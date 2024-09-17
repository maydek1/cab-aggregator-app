package com.example.passengerservice.service.ServiceImpl;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.exception.EmailAlreadyExistException;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.exception.PhoneAlreadyExistException;
import com.example.passengerservice.mapper.PassengerMapper;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repositories.PassengerRepository;
import com.example.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.passengerservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerResponse getPassengerById(Long id) {
        return getOrElseThrow(id);
    }

    @Override
    public PassengerResponse deletePassengerById(Long id) {
        PassengerResponse passengerResponse = getOrElseThrow(id);
        passengerRepository.deleteById(id);
        return passengerResponse;
    }

    @Override
    public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findById(id)
                        .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));

        updateEmailIfChanged(passenger.getEmail(), passengerRequest.getEmail(), () -> passenger.setEmail(passengerRequest.getEmail()));

        updatePhoneIfChanged(passenger.getPhone(), passengerRequest.getPhone(), () -> passenger.setPhone(passengerRequest.getPhone()));

        passenger.setFirstName(passengerRequest.getFirstName());
        passenger.setSecondName(passengerRequest.getSecondName());

        passengerRepository.save(passenger);
        return passengerMapper.passengerToPassengerResponse(passenger);
    }

    private void updateEmailIfChanged(String currentEmail, String newEmail, Runnable updateAction) {
        if (!Objects.equals(currentEmail, newEmail)) {
            updateAction.run();
        } else {
            throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, newEmail));
        }
    }

    private void updatePhoneIfChanged(String currentPhone, String newPhone, Runnable updateAction) {
        if (!Objects.equals(currentPhone, newPhone)) {
            updateAction.run();
        } else {
            throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, newPhone));
        }
    }

    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        Passenger passenger = passengerMapper.passengerRequestToPassenger(passengerRequest);
        passengerRepository.save(passenger);
        return passengerMapper.passengerToPassengerResponse(passenger);
    }

    PassengerResponse getOrElseThrow(Long id){
        return passengerMapper.passengerToPassengerResponse(passengerRepository.findById(id)
                .orElseThrow(()-> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, id))));
    }
}
