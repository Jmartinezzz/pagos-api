package com.apipagos.pagos.Dto;

import com.apipagos.pagos.Enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPaymentDto {

    private double cantidad;
    private PaymentType tipoPago;
    private LocalDate fecha;
    private String codigo_estudiante;
    
}
