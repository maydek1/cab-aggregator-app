package com.software.modsen.driverservice.component.steps;

import com.software.modsen.driverservice.client.CarClient;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.exceptions.CarNotFoundException;
import com.software.modsen.driverservice.exceptions.DriverNotFoundException;
import com.software.modsen.driverservice.exceptions.EmailAlreadyExistException;
import com.software.modsen.driverservice.mapper.DriverMapper;
import io.cucumber.spring.CucumberContextConfiguration;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.DriverService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;

@CucumberContextConfiguration
public class DriverSteps {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private CarClient carClient;

    private Driver driver;
    private Exception exception;
    private List<Driver> driversList;

    @Given("a driver with id {long}, email {string} and carId {long} exists")
    public void givenDriverExists(Long id, String email, Long carId) {
        driver = new Driver();
        driver.setId(id);
        driver.setEmail(email);
        driver.setCarId(carId);
        driver.setAvailable(true);
    }

    @When("I request to get driver by id {long}")
    public void getDriverById(Long id) {
        try {
            driver = driverService.getDriverById(id);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("I should receive a driver with email {string} and carId {long}")
    public void shouldReceiveDriverWithDetails(String email, Long carId) {
        assertNotNull(driver);
        assertEquals(email, driver.getEmail());
        assertEquals(carId, driver.getCarId());
    }

    @When("I request to get a non-existent driver by id {long}")
    public void getNonExistentDriverById(Long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
        try {
            driverService.getDriverById(id);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("I should get a DriverNotFoundException")
    public void shouldGetDriverNotFoundException() {
        assertTrue(exception instanceof DriverNotFoundException);
    }

    @When("I delete driver with id {long}")
    public void deleteDriverById(Long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        driverService.deleteDriverById(id);
        Mockito.verify(driverRepository).deleteById(id);
    }

    @When("I try to get this driver")
    public void tryToGetDeletedDriver() {
        when(driverRepository.findById(driver.getId())).thenReturn(Optional.empty());
        try {
            driverService.getDriverById(driver.getId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I update driver with id {long} to change email to {string}")
    public void updateDriver(Long id, String newEmail) {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(newEmail);
        Driver newDriver = new Driver();
        newDriver.setEmail(newEmail);
        newDriver.setId(id);
        newDriver.setCarId(driver.getCarId());

        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        when(driverMapper.driverRequestToDriver(any())).thenReturn(newDriver);
        when(driverRepository.save(any())).thenReturn(newDriver);

        driver = driverService.updateDriver(id, driverRequest);
    }

    @Then("I should get an EmailAlreadyExistException")
    public void shouldGetEmailAlreadyExistException() {
        assertTrue(exception instanceof EmailAlreadyExistException);
    }

    @When("I create a driver with id {long}, email {string} and carId {long}")
    public void createDriver(Long id, String email, Long carId) {
        CarResponse carResponse = new CarResponse();
        carResponse.setId(carId);

        driver = new Driver();
        driver.setId(id);
        driver.setEmail(email);
        driver.setCarId(carId);

        when(carClient.getCarById(any())).thenReturn(ResponseEntity.ok(carResponse));
        when(driverMapper.driverRequestToDriver(any())).thenReturn(driver);
        when(driverRepository.save(any())).thenReturn(driver);

        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(email);
        driverRequest.setCarId(carId);

        driver = driverService.createDriver(driverRequest);
    }

    @When("I create a driver with email {string} and non-existent carId {long}")
    public void createDriverWithNonExistentCar(String email, Long carId) {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(email);
        driverRequest.setCarId(carId);

        when(carClient.getCarById(carId)).thenReturn(null);
        try {
            driverService.createDriver(driverRequest);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I request to get all drivers")
    public void getAllDrivers() {
        when(driverRepository.findAll()).thenReturn(List.of(driver));
        driversList = driverService.getAllDrivers();
    }

    @Then("I should get a list of drivers with ids {long}")
    public void shouldGetDriversList(Long id1) {
        assertEquals(1, driversList.size());
        assertEquals(id1, driversList.get(0).getId());
    }

    @When("I toggle availability of driver with id {long}")
    public void toggleDriverAvailability(Long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any())).thenReturn(driver);
        driver = driverService.toggleAvailable(id, !driver.isAvailable());
    }

    @Then("I should receive a driver with email {string} and availability set to true")
    public void shouldReceiveDriverWithAvailability(String email) {
        assertNotNull(driver);
        assertTrue(driver.isAvailable());
        assertEquals(email, driver.getEmail());
    }

    @When("I update the rating with rate {double}")
    public void updateDriverRating(Double newRate) {
        when(driverRepository.findById(any())).thenReturn(Optional.of(driver));
        when(driverRepository.save(any())).thenReturn(driver);

        DriverRatingRequest ratingRequest = new DriverRatingRequest();
        ratingRequest.setRate(newRate);
        ratingRequest.setDriverId(driver.getId());
        driver = driverService.updateRating(ratingRequest);
    }

    @Then("I should receive a driver with email {string}, rate {double} and ratingCount {int}")
    public void shouldReceiveDriverWithUpdatedRating(String email, double rate, int ratingCount) {
        assertNotNull(driver);
        assertEquals(email, driver.getEmail());
        assertEquals(rate, driver.getRate());
        assertEquals(ratingCount, driver.getRatingCount());
    }

    @Given("a driver with id {long}, email {string} and availability set to false exists")
    public void aDriverWithIdEmailAndAvailabilitySetToFalseExists(long id, String email) {
        driver = new Driver();
        driver.setId(id);
        driver.setEmail(email);
        driver.setAvailable(false);
    }

    @Given("a driver with id {int}, email {string} and rate {int} and ratingCount {int} exists")
    public void aDriverWithIdEmailAndRateAndRatingCountExists(long id, String email, int rate, int ratingCount) {
        driver = new Driver();
        driver.setRatingCount(ratingCount);
        driver.setRate(rate);
        driver.setId(id);
        driver.setEmail(email);
    }

    @Then("I should get a CarNotFoundException")
    public void iShouldGetACarNotFoundException() {
        assertTrue(exception instanceof CarNotFoundException);
    }

    @Given("a driver with email {string} already exists")
    public void aDriverWithEmailAlreadyExists(String email) {
        driver = new Driver();
        driver.setEmail(email);
    }

    @When("I create a driver with email {string} and carId {long}")
    public void iCreateADriverWithEmailAndCarId(String email, long carId) {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(email);
        driverRequest.setCarId(carId);
        when(carClient.getCarById(carId)).thenReturn(ResponseEntity.ok(new CarResponse(1L, "dd", "bb", "red")));
        when(driverRepository.existsByEmail(any())).thenReturn(true);
        try {
            driverService.createDriver(driverRequest);
        } catch (Exception e) {
            exception = e;
        }

    }

    @When("I update driver with id {long} to change email to already exist email {string}")
    public void iUpdateDriverWithIdToChangeEmailToAlreadyExistEmail(Long id, String newEmail) {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail(newEmail);
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        when(driverRepository.existsByEmail(any())).thenReturn(true);
        when(driverMapper.driverRequestToDriver(any())).thenReturn(driver);
        try {
            driver = driverService.updateDriver(id, driverRequest);
        }
        catch (EmailAlreadyExistException ex){
            exception = ex;
        }

    }
}
