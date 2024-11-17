package com.software.modsen.passengerservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PassengerRequest {
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private BigDecimal money;
}
