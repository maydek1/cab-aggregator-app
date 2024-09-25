package com.software.modsen.driverservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private static final long REPLY_TIMEOUT = 5000;

    @Value("${rabbitmq.queues.request}")
    private String requestQueue;

    @Value("${rabbitmq.queues.response}")
    private String responseQueue;

    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueue);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(responseQueue);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(REPLY_TIMEOUT);
        return template;
    }
}