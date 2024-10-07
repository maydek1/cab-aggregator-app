package com.software.modsen.driverservice.exceptions;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(String msg){
        super(msg);
    }
}
