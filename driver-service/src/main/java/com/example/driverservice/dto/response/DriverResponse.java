package com.example.driverservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String first_name;
    private String second_name;
    private String phone;
    private String email;
    private String sex;
    private Long car_id;
}
