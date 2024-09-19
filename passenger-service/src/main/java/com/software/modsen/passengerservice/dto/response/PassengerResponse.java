package com.software.modsen.passengerservice.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PassengerResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
}
