package com.apipagos.pagos.Services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.apipagos.pagos.Entities.Payment;
import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Enums.PaymentStatus;
import com.apipagos.pagos.Enums.PaymentType;
import com.apipagos.pagos.Repositories.PaymentRespository;
import com.apipagos.pagos.Repositories.StudentRepository;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRespository paymentRepo;

    @Autowired
    private StudentRepository studentRepo;

    private static final String BASE_DIR = Paths.get(System.getProperty("user.home"), "enset-data", "payments")
            .toString();

    public Payment savePayment(MultipartFile file, double cantidad, PaymentType type, LocalDate date,
            String codigo_estudiante) throws IOException {
        Path folder_path = Paths.get(BASE_DIR);

        if (!Files.exists(folder_path)) {
            Files.createDirectories(folder_path);
        }

        String file_name = UUID.randomUUID().toString() + ".pdf";
        Path file_path = folder_path.resolve(file_name);
        Files.copy(file.getInputStream(), file_path); // you can add StandardCopyOption.REPLACE_EXISTING

        // guardar infor del estudiante
        Student student = studentRepo.findByCodigo(codigo_estudiante);
        Payment pago = Payment.builder()
                .tipoPago(type)
                .estatus(PaymentStatus.CREADO)
                .fecha(date)
                .estudiante(student)
                .cantidad(cantidad)
                .archivo(file_path.toUri().toString())
                .build();

        return paymentRepo.save(pago);

    }

    public byte[] getFileById(Long pago_id) throws IOException {
        Payment payment = paymentRepo.findById(pago_id).get();

        return Files.readAllBytes(Path.of(URI.create(payment.getArchivo())));
    }

    public Payment updatePaymentByStatus(PaymentStatus status, Long id) {
        Payment pago = paymentRepo.findById(id).get();
        pago.setEstatus(status);
        return paymentRepo.save(pago);
    }

}
