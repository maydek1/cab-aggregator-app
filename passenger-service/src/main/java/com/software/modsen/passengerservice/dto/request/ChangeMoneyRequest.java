package com.software.modsen.passengerservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangeMoneyRequest {
    private BigDecimal money;
    private Long passengerId;
}
