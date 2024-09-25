package com.software.modsen.driverservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private String sex;
    private Long car_id;
    private int rate;
    private int ratingCount;
}
