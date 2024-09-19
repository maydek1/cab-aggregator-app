package com.software.modsen.carstationservice.service;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.dto.response.CarSetResponse;

public interface CarService {
    CarResponse getCarById(Long id);
    CarResponse deleteCarById(Long id);
    CarResponse updateCar(Long id, CarRequest carRequest);
    CarResponse createCar(CarRequest carRequest);

    CarSetResponse getAllCars();
}
