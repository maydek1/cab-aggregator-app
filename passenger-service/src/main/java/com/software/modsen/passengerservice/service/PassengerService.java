package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.software.modsen.passengerservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public Passenger getPassengerById(Long id) {
        return getOrElseThrow(id);
    }

    public Passenger deletePassengerById(Long id) {
        Passenger passenger = getOrElseThrow(id);
        passengerRepository.deleteById(id);
        return passenger;
    }

    public Passenger updatePassenger(Long id, PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));
        checkEmailToExist(passenger.getEmail(), passengerRequest.getEmail());
        checkPhoneToExist(passenger.getPhone(), passengerRequest.getPhone());
        Passenger passenger1 = passengerMapper.passengerRequestToPassenger(passengerRequest);
        passenger1.setId(passenger.getId());
        return passengerRepository.save(passenger1);
    }

    private void checkEmailToExist(String currentEmail, String newEmail) {
        if (!Objects.equals(currentEmail, newEmail)) {
            if (passengerRepository.existsByEmail(newEmail)) {
                throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, newEmail));
            }
        }
    }

    private void checkPhoneToExist(String currentPhone, String newPhone) {
        if (!Objects.equals(currentPhone, newPhone)) {
            if (passengerRepository.existsByPhone(newPhone)) {
                throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, newPhone));
            }
        }
    }

    public Passenger createPassenger(PassengerRequest passengerRequest) {

        if(passengerRequest.getMoney() == null) passengerRequest.setMoney(BigDecimal.valueOf(0));

        if (passengerRepository.existsByEmail(passengerRequest.getEmail())) {
            throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, passengerRequest.getEmail()));
        }
        if (passengerRepository.existsByPhone(passengerRequest.getPhone())) {
            throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, passengerRequest.getPhone()));
        }

        Passenger passenger = passengerMapper.passengerRequestToPassenger(passengerRequest);
        return passengerRepository.save(passenger);
    }

    private Passenger getOrElseThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));
    }

    public List<Passenger> getAllPassenger() {
        return passengerRepository.findAll();
    }

    public Passenger chargeMoney(ChargeMoneyRequest chargeMoneyRequest) {
        Passenger passenger = passengerRepository.findById(chargeMoneyRequest.getPassengerId())
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND, chargeMoneyRequest.getPassengerId())));

        passenger.setMoney(passenger.getMoney().add(chargeMoneyRequest.getMoney()));
        return passengerRepository.save(passenger);
    }
}
