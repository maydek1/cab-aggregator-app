package com.software.modsen.carstationservice.controller;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.dto.response.CarSetResponse;
import com.software.modsen.carstationservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@RequestBody CarRequest carRequest) {
        return new ResponseEntity<>(carService.createCar(carRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id,
                                                       @RequestBody CarRequest carRequest) {
        return ResponseEntity.ok(carService.updateCar(id, carRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarResponse> deleteCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.deleteCarById(id));
    }

    @GetMapping
    public ResponseEntity<CarSetResponse> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }
}
