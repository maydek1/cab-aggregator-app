package com.software.modsen.passengerservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RideRequest {
    private Long driverId;
    private Long passengerId;
    private String pickupAddress;
    private String destinationAddress;
    private BigDecimal price;
}
