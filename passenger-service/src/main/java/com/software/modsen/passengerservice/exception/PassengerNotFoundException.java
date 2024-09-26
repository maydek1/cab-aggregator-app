package com.software.modsen.passengerservice.exception;

public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(String msg) {
        super(msg);
    }
}
