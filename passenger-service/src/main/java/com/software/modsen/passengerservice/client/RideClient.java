package com.software.modsen.passengerservice.client;

import com.software.modsen.passengerservice.config.FeignConfig;
import com.software.modsen.passengerservice.dto.request.RideRequest;
import com.software.modsen.passengerservice.dto.request.RideDriverRequest;
import com.software.modsen.passengerservice.dto.request.RideStatusRequest;
import com.software.modsen.passengerservice.dto.response.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ride-service", url = "http://localhost:4568/ride", configuration = FeignConfig.class)
public interface RideClient {

    @PatchMapping("/status/")
    ResponseEntity<RideResponse> changeStatus(@RequestBody RideStatusRequest rideStatusRequest);

    @PostMapping()
    ResponseEntity<RideResponse> createRide(@RequestBody RideRequest rideRequest);

    @PutMapping("/driver")
    ResponseEntity<RideResponse> setDriver(@RequestBody RideDriverRequest rideStatusRequest);

}
