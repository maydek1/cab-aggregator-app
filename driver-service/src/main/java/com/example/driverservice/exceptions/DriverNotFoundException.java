package com.example.driverservice.exceptions;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(String msg){
        super(msg);
    }
}
