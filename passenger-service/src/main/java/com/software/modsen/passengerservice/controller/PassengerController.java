package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.controller.swagger.PassengerApi;
import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponseSet;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController implements PassengerApi {

    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PASSENGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.getPassengerById(id));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<PassengerResponseSet> getAllPassengers() {
        PassengerResponseSet passengerResponseSet = new PassengerResponseSet(
                passengerService.getAllPassenger()
                        .stream()
                        .map(passengerMapper::passengerToPassengerResponse)
                        .collect(Collectors.toSet()));
        return ResponseEntity.ok(passengerResponseSet);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PASSENGER')")
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        System.out.println(passengerRequest.toString());
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.createPassenger(passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.updatePassenger(id, passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PassengerResponse> deletePassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.deletePassengerById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(passengerResponse);
    }

    @Override
    @PostMapping("/money")
    @PreAuthorize("hasRole('ROLE_PASSENGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PassengerResponse> chargeMoney(@RequestBody ChargeMoneyRequest chargeMoneyRequest) {
        return ResponseEntity.ok(passengerMapper.passengerToPassengerResponse(passengerService.chargeMoney(chargeMoneyRequest)));
    }
}
