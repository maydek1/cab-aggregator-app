package com.software.modsen.driverservice.exceptions;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String msg){
        super(msg);
    }
}
