package com.software.modsen.carstationservice.dto.response;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarSetResponse {
    private Set<CarResponse> carResponseList;
}
