package com.software.modsen.carstationservice.service;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.exceptions.CarNotFoundException;
import com.software.modsen.carstationservice.exceptions.CarNumberAlreadyExistException;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.model.Car;
import com.software.modsen.carstationservice.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarService carService;

    private Car car;
    private CarRequest carRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car = new Car();
        car.setId(1L);
        car.setNumber("ABC123");

        carRequest = new CarRequest();
        carRequest.setNumber("BM2_12");
    }

    @Test
    void getCarById_ShouldReturnCar_WhenCarExists() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Car result = carService.getCarById(1L);
        assertEquals(car, result);
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void getCarById_ShouldThrowCarNotFoundException_WhenCarDoesNotExist() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.getCarById(1L));
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void deleteCarById_ShouldDeleteCar_WhenCarExists() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Car deletedCar = carService.deleteCarById(1L);
        assertEquals(car, deletedCar);
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCarById_ShouldThrowCarNotFoundException_WhenCarDoesNotExist() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.deleteCarById(1L));
        verify(carRepository, never()).deleteById(1L);
    }

    @Test
    void updateCar_ShouldUpdateCar_WhenCarExists() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.carRequestToCar(carRequest)).thenReturn(new Car());
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car updatedCar = carService.updateCar(1L, carRequest);
        assertNotNull(updatedCar);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_ShouldThrowCarNotFoundException_WhenCarDoesNotExist() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.updateCar(1L, carRequest));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void updateCar_ShouldThrowCarNumberAlreadyExistException_WhenNumberExists() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.existsByNumber("BM2_12")).thenReturn(true);

        assertThrows(CarNumberAlreadyExistException.class, () -> carService.updateCar(1L, carRequest));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void createCar_ShouldCreateCar_WhenNumberDoesNotExist() {
        when(carRepository.existsByNumber("BM2_12")).thenReturn(false);
        when(carMapper.carRequestToCar(carRequest)).thenReturn(new Car());
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car createdCar = carService.createCar(carRequest);
        assertNotNull(createdCar);
        verify(carRepository, times(1)).existsByNumber("BM2_12");
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void createCar_ShouldThrowCarNumberAlreadyExistException_WhenNumberExists() {
        when(carRepository.existsByNumber("BM2_12")).thenReturn(true);
        assertThrows(CarNumberAlreadyExistException.class, () -> carService.createCar(carRequest));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void getAllCars_ShouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(car));
        List<Car> cars = carService.getAllCars();
        assertEquals(1, cars.size());
        verify(carRepository, times(1)).findAll();
    }
}
