package com.software.modsen.driverservice.client;


import com.software.modsen.driverservice.config.FeignConfig;
import com.software.modsen.driverservice.dto.response.CarResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${service.car-service.name}", path = "${service.car-service.path}", configuration = FeignConfig.class)
public interface CarClient {

    @GetMapping("/{id}")
    ResponseEntity<CarResponse> getCarById(@PathVariable Long id);
}
