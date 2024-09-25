package com.software.modsen.passengerservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RatingRequest {
    private Long rideId;
    private String comment;
    private int rate;
}
