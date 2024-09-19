package com.software.modsen.driverservice.dto.response;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverSetResponse {
    private Set<DriverResponse> driverResponseList;
}
