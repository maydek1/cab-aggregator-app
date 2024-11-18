package com.software.modsen.passengerservice.controller.swagger;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponseSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface PassengerApi {

    @Operation(summary = "Получить пассажира по ID")
    @ApiResponse(responseCode = "200", description = "Пассажир найден",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @GetMapping("/{id}")
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);

    @Operation(summary = "Получить всех пассажиров")
    @ApiResponse(responseCode = "200", description = "Пассажиры успешно получены",
            content = @Content(schema = @Schema(implementation = PassengerResponseSet.class)))
    @GetMapping
    ResponseEntity<PassengerResponseSet> getAllPassengers();

    @Operation(summary = "Создать нового пассажира")
    @ApiResponse(responseCode = "201", description = "Пассажир успешно создан",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @PostMapping
    ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest);

    @Operation(summary = "Обновить информацию о пассажире")
    @ApiResponse(responseCode = "200", description = "Пассажир успешно обновлён",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @PutMapping("/{id}")
    ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id, @RequestBody PassengerRequest passengerRequest);

    @Operation(summary = "Удалить пассажира по ID")
    @ApiResponse(responseCode = "204", description = "Пассажир успешно удалён")
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<PassengerResponse> deletePassengerById(@PathVariable Long id);

    @Operation(summary = "Пополнить счёт пассажира")
    @ApiResponse(responseCode = "200", description = "Счёт пассажира успешно пополнен",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @PostMapping("/money")
    ResponseEntity<PassengerResponse> chargeMoney(@RequestBody ChargeMoneyRequest chargeMoneyRequest);
}
