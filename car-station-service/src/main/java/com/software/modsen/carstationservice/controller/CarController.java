package com.software.modsen.carstationservice.controller;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.dto.response.CarSetResponse;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(
                carMapper.carToCarResponse(carService.getCarById(id)));
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@RequestBody CarRequest carRequest) {
        return new ResponseEntity<>(carMapper.carToCarResponse(carService.createCar(carRequest)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id,
                                                 @RequestBody CarRequest carRequest) {
        return ResponseEntity.ok(carMapper.carToCarResponse(carService.updateCar(id, carRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarResponse> deleteCarById(@PathVariable Long id) {
        return ResponseEntity.ok( carMapper.carToCarResponse(carService.deleteCarById(id)));
    }

    @GetMapping
    public ResponseEntity<CarSetResponse> getAllCars() {
        return ResponseEntity.ok(new CarSetResponse(carService.getAllCars().stream()
                .map(carMapper::carToCarResponse)
                .collect(Collectors.toSet())));
    }
}
