package com.software.modsen.passengerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RatingResponse {
    private Long id;
    private Long rideId;
    private String comment;
    private int rate;
}
