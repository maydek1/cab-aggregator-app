package com.software.modsen.carstationservice.controller;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.dto.response.CarSetResponse;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @Operation(summary = "Получить машину по ID")
    @ApiResponse(responseCode = "200", description = "Машина найдена",
            content = @Content(schema = @Schema(implementation = CarResponse.class)))
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(
                carMapper.carToCarResponse(carService.getCarById(id)));
    }

    @Operation(summary = "Создать машину")
    @ApiResponse(responseCode = "201", description = "Машина успешно создана",
            content = @Content(schema = @Schema(implementation = CarResponse.class)))
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> createCar(@RequestBody CarRequest carRequest) {
        return new ResponseEntity<>(carMapper.carToCarResponse(carService.createCar(carRequest)),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить информацию о машине")
    @ApiResponse(responseCode = "200", description = "Машина успешно обновлена",
            content = @Content(schema = @Schema(implementation = CarResponse.class)))
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id,
                                                 @RequestBody CarRequest carRequest) {
        return ResponseEntity.ok(carMapper.carToCarResponse(carService.updateCar(id, carRequest)));
    }

    @Operation(summary = "Удалить машину по ID")
    @ApiResponse(responseCode = "200", description = "Машина успешно удалена",
            content = @Content(schema = @Schema(implementation = CarResponse.class)))
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CarResponse> deleteCarById(@PathVariable Long id) {

        return ResponseEntity.ok(carMapper.carToCarResponse(carService.deleteCarById(id)));
    }

    @Operation(summary = "Получить список всех машин")
    @ApiResponse(responseCode = "200", description = "Машины успешно получены",
            content = @Content(schema = @Schema(implementation = CarSetResponse.class)))
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CarSetResponse> getAllCars() {
        return ResponseEntity.ok(new CarSetResponse(carService.getAllCars().stream()
                .map(carMapper::carToCarResponse)
                .collect(Collectors.toSet())));
    }
}
