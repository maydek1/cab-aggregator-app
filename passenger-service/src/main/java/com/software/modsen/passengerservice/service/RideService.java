package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.client.RideClient;
import com.software.modsen.passengerservice.dto.request.RideRequest;
import com.software.modsen.passengerservice.dto.request.RideDriverRequest;
import com.software.modsen.passengerservice.dto.response.RideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.software.modsen.passengerservice.util.ExceptionMessages.AVAILABLE_DRIVER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RideService {

    public static final int MESSAGE_TIMEOUT = 5000;

    @Value("${rabbitmq.queues.request}")
    private String requestQueue;

    private final RideClient rideClient;
    private final RabbitTemplate rabbitTemplate;
    private final Queue responseQueue;

    public RideResponse startRide(RideRequest rideRequest) {
        RideResponse rideResponse = rideClient.createRide(rideRequest).getBody();
        Long driverId = findAvailableDriver();
        assert rideResponse != null;
        rideResponse = rideClient.setDriver(new RideDriverRequest(driverId, rideResponse.getId())).getBody();

        return rideResponse;
    }

    public Long findAvailableDriver() {

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(responseQueue.getName());
        Message message = new Message("".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(requestQueue, message);

        Message responseMessage = rabbitTemplate.receive(responseQueue.getName(), MESSAGE_TIMEOUT);
        if (responseMessage != null) {
            return Long.valueOf(new String(responseMessage.getBody()));
        } else {
            throw new RuntimeException(AVAILABLE_DRIVER_NOT_FOUND);
        }
    }
}

