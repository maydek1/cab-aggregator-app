package com.software.modsen.carstationservice.exceptions;

public class CarNumberAlreadyExistException extends RuntimeException{
    public CarNumberAlreadyExistException(String msg){
        super(msg);
    }
}
