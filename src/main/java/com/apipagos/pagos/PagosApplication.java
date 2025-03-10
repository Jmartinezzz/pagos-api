package com.apipagos.pagos;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apipagos.pagos.Entities.Payment;
import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Enums.PaymentStatus;
import com.apipagos.pagos.Enums.PaymentType;
import com.apipagos.pagos.Repositories.PaymentRespository;
import com.apipagos.pagos.Repositories.StudentRepository;

@SpringBootApplication
public class PagosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagosApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository studentRepo, PaymentRespository paymentRespo) {
		return args -> {
			paymentRespo.deleteAll();  // Borra todos los pagos
            studentRepo.deleteAll();

			studentRepo.save(Student.builder()
				.id(UUID.randomUUID().toString())
				.nombre("jorge")
				.apellido("paz")
				.codigo("500")
				.programId("aws")
				.build()
				);
			studentRepo.save(Student.builder()
				.id(UUID.randomUUID().toString())
				.nombre("casildo")
				.apellido("montoya")
				.codigo("501")
				.programId("aws2")
				.build()
				);
			studentRepo.save(Student.builder()
				.id(UUID.randomUUID().toString())
				.nombre("wario")
				.apellido("estadio")
				.codigo("502")
				.programId("aws2")
				.build()
				);

			PaymentType tiposPago[] = PaymentType.values();
			Random rnd = new Random();

			studentRepo.findAll().forEach(student -> {
				for (int i = 0; i < 10; i++) {
					int index = rnd.nextInt(tiposPago.length);
					Payment pago = Payment.builder()
						.cantidad(100 + (int)(Math.random()*2000))
						.tipoPago(tiposPago[index])
						.estatus(PaymentStatus.CREADO)
						.fecha(LocalDate.now())
						.estudiante(student)
						.build();
					paymentRespo.save(pago);
				}
			});
		};
	}

}
