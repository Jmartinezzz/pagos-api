package com.apipagos.pagos.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apipagos.pagos.Entities.Payment;
import com.apipagos.pagos.Enums.PaymentStatus;
import com.apipagos.pagos.Enums.PaymentType;

@Repository
public interface PaymentRespository extends JpaRepository<Payment, Long> {

    List<Payment> findByEstudianteCodigo(String codigo);
    List<Payment> findByEstatus(PaymentStatus estatus);
    List<Payment> findByTipoPago(PaymentType tipoPago);
    
}
