package com.apipagos.pagos.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apipagos.pagos.Entities.Payment;
import com.apipagos.pagos.Enums.PaymentStatus;
import com.apipagos.pagos.Enums.PaymentType;
import com.apipagos.pagos.Repositories.PaymentRespository;
import com.apipagos.pagos.Services.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin("*")
@RequestMapping("api")
public class PaymentController {
    
    @Autowired
    private PaymentRespository paymentRepo;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/pagos")
    public List<Payment> paymentList() {
        return paymentRepo.findAll();
    }

    @GetMapping("/pagos/{id}")
    public Payment paymentById(@PathVariable Long id) {
        return paymentRepo.findById(id).get();
    }

    @GetMapping("/estudiantes/{codigo}/pagos")
    public List<Payment> PaymentsByStudentCode(@PathVariable String codigo) {
        return paymentRepo.findByEstudianteCodigo(codigo);
    }

    @GetMapping("/pagos-por-status")
    public List<Payment> paymentsByStatus(@RequestParam PaymentStatus status) {
        return paymentRepo.findByEstatus(status);
    }

    @GetMapping("/pagos/por-tipo")
    public List<Payment> paymentByType(@RequestParam PaymentType type) {
        return paymentRepo.findByTipoPago(type);
    }
    
    
    @PutMapping("/pagos/{pagoid}")
    public Payment updatePaymentStatus(@PathVariable Long pagoid, @RequestParam PaymentStatus status) {    
        return paymentService.updatePaymentByStatus(status, pagoid);
    }

    @PostMapping(path = "/pagos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment save(@RequestParam MultipartFile file, double cantidad, PaymentType type, LocalDate date, String codigo_estudiante) throws Exception{
        return paymentService.savePayment(file, cantidad, type, date, codigo_estudiante);
    }

    @GetMapping(value = "/pagoFile/{pago_id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] filesById(@PathVariable Long pago_id) throws Exception {
        return paymentService.getFileById(pago_id);
    }
    
}
