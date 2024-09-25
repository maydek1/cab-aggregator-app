package com.software.modsen.driverservice.client;

import com.software.modsen.driverservice.config.FeignConfig;
import com.software.modsen.driverservice.dto.response.RideResponse;
import com.software.modsen.driverservice.dto.response.RideResponseSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ride-service", url = "http://localhost:4568/ride", configuration = FeignConfig.class)
public interface RideClient {

    @GetMapping("/created/{id}")
    RideResponseSet getRideToConfirm(@PathVariable Long id);

    @PostMapping("/accept/{id}")
    RideResponse acceptRide(@PathVariable Long id);

    @PostMapping("/reject/{id}")
    RideResponse rejectRide(@PathVariable Long id);

    @PostMapping("/completed/{id}")
    RideResponse completedRide(@PathVariable Long id);

}
