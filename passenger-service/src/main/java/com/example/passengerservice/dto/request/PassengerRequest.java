package com.example.passengerservice.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PassengerRequest {
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
}
