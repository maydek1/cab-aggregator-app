package com.example.carstationservice.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    private String model;
    private String number;
    private String color;
}
