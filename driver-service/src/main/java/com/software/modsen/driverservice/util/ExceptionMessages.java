package com.software.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String DRIVER_NOT_FOUND = "Not found driver with id: '%s'";
    public static final String EMAIL_ALREADY_EXIST = "Driver with email '%s' already exist";
    public static final String CAR_NOT_FOUND = "Not found car with id: '%s'";
    public static final String PHONE_ALREADY_EXIST = "Driver with phone '%s' already exist";
}
