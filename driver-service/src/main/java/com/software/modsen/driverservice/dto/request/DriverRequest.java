package com.software.modsen.driverservice.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DriverRequest {
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private String sex;
    private Long carId;
}
