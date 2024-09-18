package com.example.carstationservice.exceptions;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(String msg){
        super(msg);
    }
}
