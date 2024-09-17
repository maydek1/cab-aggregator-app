package com.example.passengerservice.service;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.repositories.PassengerRepository;

public interface PassengerService {
    PassengerRepository getPassengerById(Long id);
    PassengerRepository deletePassengerById(Long id);
    PassengerRepository updatePassenger(Long id, PassengerRequest passengerRequest);
    PassengerRequest createPassenger(PassengerRequest passengerRequest);
}
