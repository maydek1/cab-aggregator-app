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

        checkEmailToExist(passenger.getEmail(), passengerRequest.getEmail());
        checkPhoneToExist(passenger.getPhone(), passengerRequest.getPhone());

        Passenger passenger1 = passengerMapper.passengerRequestToPassenger(passengerRequest);
        passenger1.setId(passenger.getId());
        return passengerMapper.passengerToPassengerResponse(passengerRepository.save(passenger1));
    }

    private void checkEmailToExist(String currentEmail, String newEmail) {
        if (!Objects.equals(currentEmail, newEmail)) {
            if (passengerRepository.existsByEmail(newEmail)){
                throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, newEmail));
            }
        }
    }

    private void checkPhoneToExist(String currentPhone, String newPhone) {
        if (!Objects.equals(currentPhone, newPhone)) {
            if (passengerRepository.existsByPhone(newPhone)){
                throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, newPhone));
            }
        }
    }

    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {

        if (passengerRepository.existsByEmail(passengerRequest.getEmail())) {
            throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, passengerRequest.getEmail()));
        }
        if (passengerRepository.existsByPhone(passengerRequest.getPhone())) {
            throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, passengerRequest.getPhone()));
        }

        Passenger passenger = passengerMapper.passengerRequestToPassenger(passengerRequest);
        passengerRepository.save(passenger);
        return passengerMapper.passengerToPassengerResponse(passenger);
    }

    PassengerResponse getOrElseThrow(Long id){
        return passengerMapper.passengerToPassengerResponse(passengerRepository.findById(id)
                .orElseThrow(()-> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, id))));
    }
}
