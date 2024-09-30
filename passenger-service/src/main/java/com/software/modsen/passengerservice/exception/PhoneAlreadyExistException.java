package com.software.modsen.passengerservice.exception;

public class PhoneAlreadyExistException extends RuntimeException {
    public PhoneAlreadyExistException(String msg) {
        super(msg);
    }
}
