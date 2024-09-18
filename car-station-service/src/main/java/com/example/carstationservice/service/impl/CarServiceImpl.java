package com.example.carstationservice.service.impl;

import com.example.carstationservice.dto.request.CarRequest;
import com.example.carstationservice.dto.response.CarResponse;
import com.example.carstationservice.dto.response.CarSetResponse;
import com.example.carstationservice.exceptions.CarNotFoundException;
import com.example.carstationservice.exceptions.CarNumberAlreadyExistException;
import com.example.carstationservice.mapper.CarMapper;
import com.example.carstationservice.model.Car;
import com.example.carstationservice.repository.CarRepository;
import com.example.carstationservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.carstationservice.util.ExceptionMessages.CAR_NOT_FOUND;
import static com.example.carstationservice.util.ExceptionMessages.CAR_NUMBER_ALREADY_EXIST;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponse getCarById(Long id) {
        return getOrElseThrow(id);
    }

    @Override
    public CarResponse deleteCarById(Long id) {
        CarResponse carResponse = getOrElseThrow(id);
        carRepository.deleteById(id);
        return carResponse;
    }

    @Override
    public CarResponse updateCar(Long id, CarRequest carRequest) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND, id)));

        checkNumberToExist(car.getNumber(), carRequest.getNumber());

        Car car1 = carMapper.carRequestToCar(carRequest);
        car1.setId(car.getId());
        return carMapper.carToCarResponse(carRepository.save(car1));
    }


    @Override
    public CarResponse createCar(CarRequest carRequest) {
        if (carRepository.existsByNumber(carRequest.getNumber())) {
            throw new CarNumberAlreadyExistException(String.format(CAR_NUMBER_ALREADY_EXIST, carRequest.getNumber()));
        }

        Car car = carMapper.carRequestToCar(carRequest);
        carRepository.save(car);
        return carMapper.carToCarResponse(car);
    }

    private CarResponse getOrElseThrow(Long id){
        return carMapper.carToCarResponse(carRepository.findById(id)
                .orElseThrow(()-> new CarNotFoundException(String.format(CAR_NOT_FOUND, id))));
    }

    @Override
    public CarSetResponse getAllCars(){
        Set<CarResponse> cars = carRepository.findAll()
                .stream()
                .map(carMapper::carToCarResponse)
                .collect(Collectors.toSet());
        return new CarSetResponse(cars);
    }

    private void checkNumberToExist(String currentNumber, String newNumber) {
        if (!Objects.equals(currentNumber, newNumber)) {
            if (carRepository.existsByNumber(newNumber)){
                throw new CarNumberAlreadyExistException(String.format(CAR_NUMBER_ALREADY_EXIST, newNumber));
            }
        }
    }
}
