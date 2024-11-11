package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @Operation(summary = "Получить водителя по ID")
    @ApiResponse(responseCode = "200", description = "Водитель найден",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.getDriverById(id)));
    }

    @Operation(summary = "Создать нового водителя")
    @ApiResponse(responseCode = "201", description = "Водитель успешно создан",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> createDriver(@RequestBody DriverRequest driverRequest) {
        return new ResponseEntity<>(driverMapper.driverToDriverResponse(driverService.createDriver(driverRequest)), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить информацию о водителе")
    @ApiResponse(responseCode = "200", description = "Водитель успешно обновлён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                                       @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.updateDriver(id, driverRequest)));
    }

    @Operation(summary = "Удалить водителя по ID")
    @ApiResponse(responseCode = "200", description = "Водитель успешно удалён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> deleteDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.deleteDriverById(id)));
    }

    @Operation(summary = "Получить список всех водителей")
    @ApiResponse(responseCode = "200", description = "Водители успешно получены",
            content = @Content(schema = @Schema(implementation = DriverSetResponse.class)))
    @GetMapping
    public ResponseEntity<DriverSetResponse> getAllDrivers() {
        return ResponseEntity.ok(new DriverSetResponse(driverService.getAllDrivers()
                .stream()
                .map(driverMapper::driverToDriverResponse)
                .collect(Collectors.toSet())));
    }

    @Operation(summary = "Получить свободного водителя")
    @ApiResponse(responseCode = "200", description = "Свободный водитель найден",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @GetMapping("/free")
    @PreAuthorize("hasRole('ROLE_PASSENGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> getFreeDriver() {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.getAvailableDriver()));
    }

    @Operation(summary = "Обновить рейтинг водителя")
    @ApiResponse(responseCode = "200", description = "Рейтинг водителя обновлён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @PostMapping("/rating")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PASSENGER')")
    public ResponseEntity<DriverResponse> updateRating(@RequestBody DriverRatingRequest driverRatingRequest) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.updateRating(driverRatingRequest)));
    }

    @Operation(summary = "Изменить доступность водителя")
    @ApiResponse(responseCode = "200", description = "Доступность водителя изменена",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @PostMapping("/available/{id}/{available}")
    public ResponseEntity<DriverResponse> toggleAvailable(@PathVariable Long id, @PathVariable boolean available) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.toggleAvailable(id, available)));
    }
}
