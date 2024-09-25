package com.software.modsen.passengerservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String pickupAddress;
    private String destinationAddress;
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
