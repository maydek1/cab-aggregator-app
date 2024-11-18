package com.software.modsen.driverservice.controller.swagger;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface DriverApi {

    @Operation(summary = "Получить водителя по ID")
    @ApiResponse(responseCode = "200", description = "Водитель найден",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @GetMapping("/{id}")
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

    @Operation(summary = "Создать нового водителя")
    @ApiResponse(responseCode = "201", description = "Водитель успешно создан",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @PostMapping
    ResponseEntity<DriverResponse> createDriver(@RequestBody DriverRequest driverRequest);

    @Operation(summary = "Обновить информацию о водителе")
    @ApiResponse(responseCode = "200", description = "Водитель успешно обновлён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @PutMapping("/{id}")
    ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                                @RequestBody DriverRequest driverRequest);

    @Operation(summary = "Удалить водителя по ID")
    @ApiResponse(responseCode = "200", description = "Водитель успешно удалён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<DriverResponse> deleteDriverById(@PathVariable Long id);

    @Operation(summary = "Получить список всех водителей")
    @ApiResponse(responseCode = "200", description = "Водители успешно получены",
            content = @Content(schema = @Schema(implementation = DriverSetResponse.class)))
    @GetMapping
    ResponseEntity<DriverSetResponse> getAllDrivers();

    @Operation(summary = "Получить свободного водителя")
    @ApiResponse(responseCode = "200", description = "Свободный водитель найден",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @GetMapping("/free")
    ResponseEntity<DriverResponse> getFreeDriver();

    @Operation(summary = "Обновить рейтинг водителя")
    @ApiResponse(responseCode = "200", description = "Рейтинг водителя обновлён",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @PostMapping("/rating")
    ResponseEntity<DriverResponse> updateRating(@RequestBody DriverRatingRequest driverRatingRequest);

    @Operation(summary = "Изменить доступность водителя")
    @ApiResponse(responseCode = "200", description = "Доступность водителя изменена",
            content = @Content(schema = @Schema(implementation = DriverResponse.class)))
    @ApiResponse(responseCode = "404", description = "Водитель не найден")
    @PostMapping("/available/{id}/{available}")
    ResponseEntity<DriverResponse> toggleAvailable(@PathVariable Long id, @PathVariable boolean available);
}
