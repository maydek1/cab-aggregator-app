package com.software.modsen.driverservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {
    private Long id;
    private String model;
    private String number;
    private String color;
}
