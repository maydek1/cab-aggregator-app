package com.software.modsen.passengerservice.client;

import com.software.modsen.passengerservice.config.FeignConfig;
import com.software.modsen.passengerservice.dto.request.RatingRequest;
import com.software.modsen.passengerservice.dto.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${rating-service.name}", url = "${rating-service.url}", configuration = FeignConfig.class)
public interface RatingClient {
    @PostMapping
    ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest ratingRequest);
}
