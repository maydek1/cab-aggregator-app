package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverListener {

    private final DriverRepository driverRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${rabbitmq.queues.request}")
    public void processRequest(Message requestMessage) {
        Long driverId = driverRepository.findFirstByAvailableIs(true).getId();

        String replyTo = requestMessage.getMessageProperties().getReplyTo();
        String correlationId = requestMessage.getMessageProperties().getCorrelationId();

        MessageProperties responseProperties = new MessageProperties();
        responseProperties.setCorrelationId(correlationId);

        Message responseMessage = new Message(driverId.toString().getBytes(), responseProperties);
        rabbitTemplate.convertAndSend(replyTo, responseMessage);
    }
}