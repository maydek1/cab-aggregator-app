package com.software.modsen.carstationservice.component.steps;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.exceptions.CarNotFoundException;
import com.software.modsen.carstationservice.exceptions.CarNumberAlreadyExistException;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.model.Car;
import com.software.modsen.carstationservice.repository.CarRepository;
import com.software.modsen.carstationservice.service.CarService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.carstationservice.util.ExceptionMessages.CAR_NUMBER_ALREADY_EXIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class CarSteps {

    @InjectMocks
    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;

    private Car car;
    private Car existingCar;
    private String carNumber;
    private CarResponse carResponse;
    private Exception exception;

    @Given("a car with id {long} exists")
    public void aCarWithIdExists(long id) {
        car = new Car();
        car.setId(id);
    }

    @When("I request to get the car by id {long}")
    public void iRequestToGetTheCarById(long id) {
        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        car = carService.getCarById(id);
    }

    @Then("I should receive a car with id {long}")
    public void iShouldReceiveACarWithId(long id) {
        Assertions.assertEquals(car.getId(), id);
    }

    @When("I delete the car by id {long}")
    public void iDeleteTheCarById(long id) {
        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        car = carService.deleteCarById(id);
    }

    @Then("the car with id {long} should not be found")
    public void theCarWithIdShouldNotBeFound(long id) {
        when(carRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(CarNotFoundException.class, () -> carService.getCarById(id));
    }

    @When("I create a new car with number {string} and model {string}")
    public void iCreateANewCarWithNumberAndModel(String number, String model) {
        car = new Car(1L, model, number, "red");
        CarRequest carRequest = new CarRequest(model, number, "red");

        when(carRepository.existsByNumber(any())).thenReturn(false);
        when(carMapper.carRequestToCar(any())).thenReturn(car);
        carService.createCar(carRequest);
    }

    @Then("the car should have number {string} and model {string}")
    public void theCarShouldHaveNumberAndModel(String number, String model) {
        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        car = carService.getCarById(1L);

        Assertions.assertEquals(car.getNumber(), number);
        Assertions.assertEquals(car.getModel(), model);
    }

    @When("I try to create a new car with number {string} and model {string}")
    public void iTryToCreateANewCarWithNumberAndModelThatAlreadyExists(String number, String model) {
        car = new Car(1L, model, number, "red");
        CarRequest carRequest = new CarRequest(model, number, "red");
        carNumber = number;

        when(carRepository.existsByNumber(any())).thenReturn(true);
        exception = Assertions.assertThrows(CarNumberAlreadyExistException.class, () -> carService.createCar(carRequest));
    }

    @Then("I should get a CarNumberAlreadyExistException")
    public void iShouldGetAnExceptionWithMessage() {
        String expectedMessage = new CarNumberAlreadyExistException(String.format(CAR_NUMBER_ALREADY_EXIST, carNumber)).getMessage();
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("a car with id {long}, number {string} and model {string}")
    public void aCarWithIdNumberAndModel(long id, String number, String model) {
        car = new Car(id, model, number, "red");
    }

    @When("I update car with id {long} to number {string}")
    public void iUpdateCarWithIdToNumber(long id, String number) {
        CarRequest carRequest = new CarRequest();
        carRequest.setNumber(number);

        Car updatedCar = new Car();
        updatedCar.setNumber(number);

        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        when(carRepository.existsByNumber(any())).thenReturn(false);
        when(carMapper.carRequestToCar(any())).thenReturn(updatedCar);
        when(carRepository.save(any())).thenReturn(updatedCar);

        car = carService.updateCar(id, carRequest);
    }

    @Then("the car with id {int} should have number {string}")
    public void theCarWithIdShouldHaveNumber(long id, String number) {
        Assertions.assertEquals(car.getId(), id);
        Assertions.assertEquals(car.getNumber(), number);
    }


    @When("I try to update car with id {long} to a number {string}")
    public void iTryToUpdateCarWithIdToNumberThatAlreadyExists(long id, String number) {
        CarRequest carRequest = new CarRequest();
        carRequest.setNumber(number);
        carNumber = number;

        Car updatedCar = new Car();
        updatedCar.setNumber(number);

        when(carRepository.findById(any())).thenReturn(Optional.ofNullable(car));
        when(carRepository.existsByNumber(any())).thenReturn(true);
        exception = Assertions.assertThrows(CarNumberAlreadyExistException.class, () -> carService.updateCar(id, carRequest));
    }

    @Then("I should get a CarNumberAlreadyExistException with message {string}")
    public void iShouldGetCarNumberAlreadyExistExceptionWithMessage(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("a second car with id {int}, number {string} and model {string}")
    public void aSecondCarWithIdNumberAndModel(long id, String number, String model) {
        existingCar = new Car(id, model, number, "red");
    }
}

