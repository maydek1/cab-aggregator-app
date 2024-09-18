package com.example.driverservice.exceptions;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String msg){
        super(msg);
    }
}
