package com.example.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String PASSENGER_NOT_FOUND = "Not found passenger with id: '%s'";
    public static final String EMAIL_ALREADY_EXIST = "Passenger with email '%s' already exist";

    public static final String PHONE_ALREADY_EXIST = "Passenger with phone '%s' already exist";
}
