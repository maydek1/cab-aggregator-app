package com.software.modsen.driverservice.exceptions;

public class PhoneAlreadyExistException extends RuntimeException{
    public PhoneAlreadyExistException(String msg){
        super(msg);
    }
}
