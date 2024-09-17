package com.example.passengerservice.service;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse getPassengerById(Long id);
    PassengerResponse deletePassengerById(Long id);
    PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);
    PassengerResponse createPassenger(PassengerRequest passengerRequest);
}
