package com.software.modsen.carstationservice.service;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.exceptions.CarNotFoundException;
import com.software.modsen.carstationservice.exceptions.CarNumberAlreadyExistException;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.model.Car;
import com.software.modsen.carstationservice.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.software.modsen.carstationservice.util.ExceptionMessages.CAR_NOT_FOUND;
import static com.software.modsen.carstationservice.util.ExceptionMessages.CAR_NUMBER_ALREADY_EXIST;


@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public Car getCarById(Long id) {
        return getOrElseThrow(id);
    }

    public Car deleteCarById(Long id) {
        Car car= getOrElseThrow(id);
        carRepository.deleteById(id);
        return car;
    }

    public Car updateCar(Long id, CarRequest carRequest) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(String.format(CAR_NOT_FOUND, id)));

        checkNumberToExist(car.getNumber(), carRequest.getNumber());

        Car car1 = carMapper.carRequestToCar(carRequest);
        car1.setId(car.getId());
        return carRepository.save(car1);
    }

    public Car createCar(CarRequest carRequest) {
        if (carRepository.existsByNumber(carRequest.getNumber())) {
            throw new CarNumberAlreadyExistException(String.format(CAR_NUMBER_ALREADY_EXIST, carRequest.getNumber()));
        }

        Car car = carMapper.carRequestToCar(carRequest);
        return carRepository.save(car);
    }

    private Car getOrElseThrow(Long id){
        return carRepository.findById(id)
                .orElseThrow(()-> new CarNotFoundException(String.format(CAR_NOT_FOUND, id)));
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    private void checkNumberToExist(String currentNumber, String newNumber) {
        if (!Objects.equals(currentNumber, newNumber)) {
            if (carRepository.existsByNumber(newNumber)){
                throw new CarNumberAlreadyExistException(String.format(CAR_NUMBER_ALREADY_EXIST, newNumber));
            }
        }
    }
}
