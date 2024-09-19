package com.software.modsen.driverservice.exceptions;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(String msg){
        super(msg);
    }
}
