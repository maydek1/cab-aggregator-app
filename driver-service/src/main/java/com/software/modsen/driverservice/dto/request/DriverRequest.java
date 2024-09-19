package com.software.modsen.driverservice.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {
    private String first_name;
    private String second_name;
    private String phone;
    private String email;
    private String sex;
    private Long car_id;
}
