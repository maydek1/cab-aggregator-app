package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repositories.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerService passengerService;

    private Passenger passenger;
    private PassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@mail.com");
        passenger.setPhone("123456789");
        passenger.setMoney(BigDecimal.valueOf(100));

        passengerRequest = new PassengerRequest();
        passengerRequest.setEmail("new@mail.com");
        passengerRequest.setPhone("987654321");
        passengerRequest.setMoney(BigDecimal.valueOf(200));
    }

    @Test
    void getPassengerById_ShouldReturnPassenger_WhenPassengerExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        Passenger result = passengerService.getPassengerById(1L);
        assertEquals(passenger, result);
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void getPassengerById_ShouldThrowPassengerNotFoundException_WhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PassengerNotFoundException.class, () -> passengerService.getPassengerById(1L));
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void deletePassengerById_ShouldDeletePassenger_WhenPassengerExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        Passenger deletedPassenger = passengerService.deletePassengerById(1L);
        assertEquals(passenger, deletedPassenger);
        verify(passengerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePassengerById_ShouldThrowPassengerNotFoundException_WhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassengerById(1L));
        verify(passengerRepository, never()).deleteById(1L);
    }

    @Test
    void updatePassenger_ShouldUpdatePassenger_WhenPassengerExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.passengerRequestToPassenger(passengerRequest)).thenReturn(new Passenger());
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        Passenger updatedPassenger = passengerService.updatePassenger(1L, passengerRequest);
        assertNotNull(updatedPassenger);
        verify(passengerRepository, times(1)).findById(1L);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void updatePassenger_ShouldThrowPassengerNotFoundException_WhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PassengerNotFoundException.class, () -> passengerService.updatePassenger(1L, passengerRequest));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void createPassenger_ShouldCreatePassenger_WhenEmailAndPhoneDoNotExist() {
        when(passengerRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(passengerRepository.existsByPhone("987654321")).thenReturn(false);
        when(passengerMapper.passengerRequestToPassenger(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        Passenger createdPassenger = passengerService.createPassenger(passengerRequest);
        assertNotNull(createdPassenger);
        verify(passengerRepository, times(1)).existsByEmail("new@mail.com");
        verify(passengerRepository, times(1)).existsByPhone("987654321");
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void createPassenger_ShouldThrowEmailAlreadyExistException_WhenEmailExists() {
        when(passengerRepository.existsByEmail("new@mail.com")).thenReturn(true);
        assertThrows(EmailAlreadyExistException.class, () -> passengerService.createPassenger(passengerRequest));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void createPassenger_ShouldThrowPhoneAlreadyExistException_WhenPhoneExists() {
        when(passengerRepository.existsByPhone("987654321")).thenReturn(true);
        assertThrows(PhoneAlreadyExistException.class, () -> passengerService.createPassenger(passengerRequest));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void getAllPassengers_ShouldReturnAllPassengers() {
        when(passengerRepository.findAll()).thenReturn(List.of(passenger));
        List<Passenger> passengers = passengerService.getAllPassenger();
        assertEquals(1, passengers.size());
        verify(passengerRepository, times(1)).findAll();
    }

    @Test
    void chargeMoney_ShouldChargeMoneySuccess(){
        ChargeMoneyRequest chargeMoneyRequest = new ChargeMoneyRequest();
        chargeMoneyRequest.setMoney(BigDecimal.valueOf(100));
        chargeMoneyRequest.setPassengerId(passenger.getId());
        BigDecimal newBalance = passenger.getMoney().add(BigDecimal.valueOf(100));

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        passenger = passengerService.chargeMoney(chargeMoneyRequest);

        assertEquals(passenger.getMoney(), newBalance);
    }
}
