package com.apipagos.pagos.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public EmailProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    // Método para enviar un mensaje con la dirección de correo y nombre del estudiante
    public void sendEmailMessage(String email, String studentName) {
        try {
            // Crear un mapa con los datos que se enviarán (correo y nombre del estudiante)
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("email", email);
            messageMap.put("studentName", studentName);

            // Convertir el mapa a un JSON
            String messageJson = objectMapper.writeValueAsString(messageMap);

            // Enviar el mensaje a la cola de RabbitMQ
            rabbitTemplate.convertAndSend("emailQueue", messageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
