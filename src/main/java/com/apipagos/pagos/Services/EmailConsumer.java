package com.apipagos.pagos.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

@Component
public class EmailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    // Método que se activa cuando hay un mensaje en la cola 'emailQueue'
    @RabbitListener(queues = "emailQueue")
    public void processEmail(String message) {
        try {
            // Convertir el mensaje JSON en un mapa de datos
            // Map<String, String> messageMap = objectMapper.readValue(message, Map.class);
            Map<String, String> messageMap = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {});

            String email = messageMap.get("email");  // Dirección de correo del destinatario
            String studentName = messageMap.get("studentName");  // Nombre del estudiante

            sendEmail(email, studentName);  // Enviar el correo al destinatario
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para enviar el correo
    private void sendEmail(String to, String studentName) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);  // Establecer el destinatario dinámico
        mailMessage.setFrom("your-test@test.com");
        mailMessage.setSubject("Nuevo Estudiante Registrado");
        mailMessage.setText("Se ha registrado un nuevo estudiante: " + studentName);

        mailSender.send(mailMessage);  // Enviar el correo
    }
}
