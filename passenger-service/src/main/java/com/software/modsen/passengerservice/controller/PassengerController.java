package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.ChangeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponseSet;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.getPassengerById(id));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PassengerResponseSet> getAllPassengers() {
        PassengerResponseSet passengerResponseSet = new PassengerResponseSet(
                passengerService.getAllPassenger()
                        .stream()
                        .map(passengerMapper::passengerToPassengerResponse)
                        .collect(Collectors.toSet()));
        return ResponseEntity.ok(passengerResponseSet);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.createPassenger(passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse passengerResponse = passengerMapper.passengerToPassengerResponse(passengerService.updatePassenger(id, passengerRequest));
        return new ResponseEntity<>(passengerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassengerById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(passengerMapper.passengerToPassengerResponse(passengerService.deletePassengerById(id)));
    }

    @PostMapping("/money")
    public ResponseEntity<PassengerResponse> changeMoney(@RequestBody ChangeMoneyRequest changeMoneyRequest){
        return ResponseEntity.ok(passengerMapper.passengerToPassengerResponse(passengerService.changeMoney(changeMoneyRequest)));
    }

}