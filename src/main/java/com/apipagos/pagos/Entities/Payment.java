package com.apipagos.pagos.Entities;

import java.time.LocalDate;

import com.apipagos.pagos.Enums.PaymentType;
import com.apipagos.pagos.Enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private double cantidad;

    private PaymentType tipoPago;

    private PaymentStatus estatus;

    private String archivo;

    @ManyToOne
    private Student estudiante;
    
}
