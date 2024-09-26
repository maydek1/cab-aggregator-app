package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.model.Driver;
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
        Driver driver = driverRepository.findFirstByAvailableIs(true);

        if (driver != null) {
            String replyTo = requestMessage.getMessageProperties().getReplyTo();
            String correlationId = requestMessage.getMessageProperties().getCorrelationId();

            MessageProperties responseProperties = new MessageProperties();
            responseProperties.setCorrelationId(correlationId);

            Message responseMessage = new Message(driver.getId().toString().getBytes(), responseProperties);
            rabbitTemplate.convertAndSend(replyTo, responseMessage);
        }
    }
}