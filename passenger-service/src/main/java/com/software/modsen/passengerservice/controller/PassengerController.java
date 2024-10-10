package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponseSet;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @Operation(summary = "Получить пассажира по ID")
    @ApiResponse(responseCode = "200", description = "Пассажир найден",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.getPassengerById(id));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @Operation(summary = "Получить всех пассажиров")
    @ApiResponse(responseCode = "200", description = "Пассажиры успешно получены",
            content = @Content(schema = @Schema(implementation = PassengerResponseSet.class)))
    @GetMapping
    public ResponseEntity<PassengerResponseSet> getAllPassengers() {
        PassengerResponseSet passengerResponseSet = new PassengerResponseSet(
                passengerService.getAllPassenger()
                        .stream()
                        .map(passengerMapper::passengerToPassengerResponse)
                        .collect(Collectors.toSet()));
        return ResponseEntity.ok(passengerResponseSet);
    }

    @Operation(summary = "Создать нового пассажира")
    @ApiResponse(responseCode = "201", description = "Пассажир успешно создан",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.createPassenger(passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить информацию о пассажире")
    @ApiResponse(responseCode = "200", description = "Пассажир успешно обновлён",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.updatePassenger(id, passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @Operation(summary = "Удалить пассажира по ID")
    @ApiResponse(responseCode = "204", description = "Пассажир успешно удалён")
    @ApiResponse(responseCode = "404", description = "Пассажир не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.deletePassengerById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(passengerResponse);
    }

    @Operation(summary = "Пополнить счёт пассажира")
    @ApiResponse(responseCode = "200", description = "Счёт пассажира успешно пополнен",
            content = @Content(schema = @Schema(implementation = PassengerResponse.class)))
    @PostMapping("/money")
    public ResponseEntity<PassengerResponse> chargeMoney(@RequestBody ChargeMoneyRequest chargeMoneyRequest) {
        return ResponseEntity.ok(passengerMapper.passengerToPassengerResponse(passengerService.chargeMoney(chargeMoneyRequest)));
    }

}
