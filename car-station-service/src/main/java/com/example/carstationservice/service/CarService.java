package com.example.carstationservice.service;

import com.example.carstationservice.dto.request.CarRequest;
import com.example.carstationservice.dto.response.CarResponse;
import com.example.carstationservice.dto.response.CarSetResponse;

public interface CarService {
    CarResponse getCarById(Long id);
    CarResponse deleteCarById(Long id);
    CarResponse updateCar(Long id, CarRequest carRequest);
    CarResponse createCar(CarRequest carRequest);

    CarSetResponse getAllCars();
}
