package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.request.RideRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.response.RideResponse;
import com.software.modsen.passengerservice.service.PassengerService;
import com.software.modsen.passengerservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final RideService rideService;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerService.getPassengerById(id);
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerService.createPassenger(passengerRequest);
        return new ResponseEntity<>(passengerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerService.updatePassenger(id, passengerRequest);
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassengerById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(passengerService.deletePassengerById(id));
    }

    @PostMapping("/ride")
    public ResponseEntity<RideResponse> startRide(@RequestBody RideRequest rideRequest){
        return ResponseEntity.ok().body(rideService.startRide(rideRequest));
    }
}