package com.apipagos.pagos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
@EnableRabbit  // Habilita la funcionalidad de RabbitListener
public class RabbitConfig {

    // Define la cola 'emailQueue'
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true);  // 'true' hace que la cola sea persistente
    }

    // Define el RabbitTemplate, usado para enviar mensajes a la cola
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    // Configura la conexión con RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }
}
