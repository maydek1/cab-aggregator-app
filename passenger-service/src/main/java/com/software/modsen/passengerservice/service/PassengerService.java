package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse getPassengerById(Long id);
    PassengerResponse deletePassengerById(Long id);
    PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);
    PassengerResponse createPassenger(PassengerRequest passengerRequest);
}
