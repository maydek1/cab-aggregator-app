package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.client.CarClient;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.exceptions.CarNotFoundException;
import com.software.modsen.driverservice.exceptions.DriverNotFoundException;
import com.software.modsen.driverservice.exceptions.EmailAlreadyExistException;
import com.software.modsen.driverservice.exceptions.PhoneAlreadyExistException;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private CarClient carClient;

    @InjectMocks
    private DriverService driverService;

    private Driver driver;
    private DriverRequest driverRequest;
    private DriverRatingRequest driverRatingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = new Driver();
        driver.setId(1L);
        driver.setEmail("test@example.com");
        driver.setPhone("123456789");
        driver.setRate(4.0);
        driver.setRatingCount(10);
        driver.setAvailable(true);

        driverRequest = new DriverRequest();
        driverRequest.setEmail("new@example.com");
        driverRequest.setPhone("987654321");
        driverRequest.setCarId(1L);

        driverRatingRequest = new DriverRatingRequest();
        driverRatingRequest.setDriverId(1L);
        driverRatingRequest.setRate(5.0);
    }

    @Test
    void getDriverById_ShouldReturnDriver_WhenDriverExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver foundDriver = driverService.getDriverById(1L);

        assertNotNull(foundDriver);
        assertEquals(driver.getId(), foundDriver.getId());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_ShouldThrowException_WhenDriverDoesNotExist() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverById(1L));
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void createDriver_ShouldCreateDriver_WhenValidRequest() {
        CarResponse carResponse = new CarResponse();
        carResponse.setId(1L);

        when(driverRepository.existsByEmail(driverRequest.getEmail())).thenReturn(false);
        when(driverRepository.existsByPhone(driverRequest.getPhone())).thenReturn(false);
        when(carClient.getCarById(1L)).thenReturn(ResponseEntity.ok(carResponse));
        when(driverMapper.driverRequestToDriver(driverRequest)).thenReturn(driver);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver createdDriver = driverService.createDriver(driverRequest);

        assertNotNull(createdDriver);
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriver_ShouldThrowCarNotFoundException_WhenCarDoesNotExist() {
        when(carClient.getCarById(1L)).thenReturn(null);

        assertThrows(CarNotFoundException.class, () -> driverService.createDriver(driverRequest));
    }

    @Test
    void createDriver_ShouldThrowEmailAlreadyExistException_WhenEmailAlreadyExists() {
        when(driverRepository.existsByEmail(driverRequest.getEmail())).thenReturn(true);
        CarResponse carResponse = new CarResponse();
        carResponse.setId(1L);
        when(carClient.getCarById(1L)).thenReturn(ResponseEntity.ok(carResponse));

        assertThrows(EmailAlreadyExistException.class, () -> driverService.createDriver(driverRequest));
    }

    @Test
    void updateDriver_ShouldUpdateDriver_WhenValidRequest() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverMapper.driverRequestToDriver(driverRequest)).thenReturn(driver);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver updatedDriver = driverService.updateDriver(1L, driverRequest);

        assertNotNull(updatedDriver);
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void deleteDriverById_ShouldDeleteDriver_WhenDriverExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver deletedDriver = driverService.deleteDriverById(1L);

        assertNotNull(deletedDriver);
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateRating_ShouldUpdateDriverRating_WhenValidRequest() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver updatedDriver = driverService.updateRating(driverRatingRequest);

        assertNotNull(updatedDriver);
        assertEquals(4.0909, updatedDriver.getRate(), 0.0001);
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void getAllDrivers_ShouldReturnListOfDrivers() {
        when(driverRepository.findAll()).thenReturn(List.of(driver));

        List<Driver> drivers = driverService.getAllDrivers();

        assertNotNull(drivers);
        assertFalse(drivers.isEmpty());
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void getAvailableDriver_ShouldReturnAvailableDriver() {
        when(driverRepository.findFirstByAvailableAndCarIdNotNull(true)).thenReturn(driver);

        driver = driverService.getAvailableDriver();

        assertTrue(driver.isAvailable());
    }

    @Test
    void toggleAvailableToDriver_ShouldReturnDriver() {
        when(driverRepository.findById(any())).thenReturn(Optional.ofNullable(driver));
        when(driverRepository.save(any())).thenReturn(driver);

        driver = driverService.toggleAvailable(driver.getId(), false);

        assertFalse(driver.isAvailable());
    }

    @Test
    void createDriver_ShouldThrowPhoneAlreadyExistException_WhenPhoneAlreadyExists() {
        when(driverRepository.existsByPhone(driverRequest.getPhone())).thenReturn(true);
        CarResponse carResponse = new CarResponse();
        carResponse.setId(1L);
        when(carClient.getCarById(1L)).thenReturn(ResponseEntity.ok(carResponse));

        assertThrows(PhoneAlreadyExistException.class, () -> driverService.createDriver(driverRequest));
    }
}
