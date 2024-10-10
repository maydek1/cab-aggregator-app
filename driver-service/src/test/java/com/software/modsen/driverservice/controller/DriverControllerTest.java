package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverController driverController;

    private Driver driver;
    private DriverRequest driverRequest;
    private DriverResponse driverResponse;
    private DriverRatingRequest driverRatingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = new Driver();
        driver.setId(1L);
        driver.setEmail("test@example.com");
        driver.setPhone("123456789");

        driverRequest = new DriverRequest();
        driverRequest.setEmail("new@example.com");
        driverRequest.setPhone("987654321");

        driverResponse = new DriverResponse();
        driverResponse.setId(1L);
        driverResponse.setEmail("test@example.com");
        driverResponse.setPhone("123456789");

        driverRatingRequest = new DriverRatingRequest();
        driverRatingRequest.setDriverId(1L);
        driverRatingRequest.setRate(5.0);
    }

    @Test
    void getDriverById_ShouldReturnDriver() {
        when(driverService.getDriverById(1L)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.getDriverById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).getDriverById(1L);
    }

    @Test
    void createDriver_ShouldReturnCreatedDriver() {
        when(driverService.createDriver(driverRequest)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.createDriver(driverRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).createDriver(driverRequest);
    }

    @Test
    void updateDriver_ShouldReturnUpdatedDriver() {
        when(driverService.updateDriver(1L, driverRequest)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.updateDriver(1L, driverRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).updateDriver(1L, driverRequest);
    }

    @Test
    void deleteDriverById_ShouldReturnDeletedDriver() {
        when(driverService.deleteDriverById(1L)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.deleteDriverById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).deleteDriverById(1L);
    }

    @Test
    void getAllDrivers_ShouldReturnDriverSet() {
        Set<DriverResponse> driverSet = Collections.singleton(driverResponse);
        when(driverService.getAllDrivers()).thenReturn(Collections.singletonList(driver));
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverSetResponse> response = driverController.getAllDrivers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverSet, Objects.requireNonNull(response.getBody()).getDrivers());
        verify(driverService, times(1)).getAllDrivers();
    }

    @Test
    void getFreeDriver_ShouldReturnFreeDriver() {
        when(driverService.getAvailableDriver()).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.getFreeDriver();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).getAvailableDriver();
    }

    @Test
    void updateRating_ShouldReturnUpdatedDriver() {
        when(driverService.updateRating(driverRatingRequest)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.updateRating(driverRatingRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).updateRating(driverRatingRequest);
    }

    @Test
    void toggleAvailable_ShouldReturnUpdatedDriver() {
        when(driverService.toggleAvailable(1L, true)).thenReturn(driver);
        when(driverMapper.driverToDriverResponse(driver)).thenReturn(driverResponse);

        ResponseEntity<DriverResponse> response = driverController.toggleAvailable(1L, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverResponse, response.getBody());
        verify(driverService, times(1)).toggleAvailable(1L, true);
    }
}
