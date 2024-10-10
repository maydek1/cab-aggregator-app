package com.software.modsen.driverservice.component.service;

import com.software.modsen.driverservice.client.CarClient;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.exceptions.CarNotFoundException;
import com.software.modsen.driverservice.exceptions.DriverNotFoundException;
import com.software.modsen.driverservice.exceptions.EmailAlreadyExistException;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private CarResponse carResponse;
    private DriverRatingRequest ratingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initializeTestObjects();
    }

    private void initializeTestObjects() {
        driver = new Driver();
        driver.setId(1L);
        driver.setEmail("test@test.com");
        driver.setPhone("123456789");
        driver.setAvailable(true);
        driver.setRate(4.0);
        driver.setRatingCount(5);

        driverRequest = new DriverRequest();
        driverRequest.setEmail("test@test.com");
        driverRequest.setPhone("123456789");
        driverRequest.setCarId(12L);

        carResponse = new CarResponse(12L, "audi s1", "bm12", "red");

        ratingRequest = new DriverRatingRequest();
        ratingRequest.setDriverId(1L);
        ratingRequest.setRate(5);
    }

    @Test
    void getDriverById_ShouldReturnDriver_WhenDriverExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver result = driverService.getDriverById(1L);

        assertEquals(driver, result);
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_ShouldThrowDriverNotFoundException_WhenDriverNotExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.getDriverById(1L));
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void deleteDriverById_ShouldDeleteDriver_WhenDriverExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver result = driverService.deleteDriverById(1L);

        assertEquals(driver, result);
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateDriver_ShouldUpdateDriver_WhenDriverExists() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverMapper.driverRequestToDriver(driverRequest)).thenReturn(new Driver());
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver result = driverService.updateDriver(1L, driverRequest);

        assertEquals(driver, result);
        verify(driverRepository, times(1)).findById(1L);
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriver_ShouldCreateNewDriver_WhenDataIsValid() {
        when(driverRepository.existsByEmail(anyString())).thenReturn(false);
        when(driverRepository.existsByPhone(anyString())).thenReturn(false);
        when(driverRepository.save(any())).thenReturn(driver);
        when(carClient.getCarById(anyLong())).thenReturn(ResponseEntity.ok(carResponse));
        when(driverMapper.driverRequestToDriver(any(DriverRequest.class))).thenReturn(driver);

        Driver result = driverService.createDriver(driverRequest);
        assertNotNull(result);

        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriver_ShouldThrowEmailAlreadyExistException_WhenEmailExists() {
        when(driverRepository.existsByEmail(anyString())).thenReturn(true);
        when(carClient.getCarById(any())).thenReturn(ResponseEntity.ok(carResponse));

        assertThrows(EmailAlreadyExistException.class, () -> driverService.createDriver(driverRequest));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void createDriver_ShouldThrowCarNotFoundException_WhenCarNotExists() {
        when(carClient.getCarById(anyLong())).thenReturn(null);

        assertThrows(CarNotFoundException.class, () -> driverService.createDriver(driverRequest));
    }

    @Test
    void getAvailableDriver_ShouldReturnDriver_WhenDriverIsAvailable() {
        when(driverRepository.findFirstByAvailableAndCarIdNotNull(true)).thenReturn(driver);

        Driver result = driverService.getAvailableDriver();

        assertEquals(driver, result);
    }

    @Test
    void toggleAvailable_ShouldUpdateDriverAvailability() {
        driver.setAvailable(false);

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver result = driverService.toggleAvailable(1L, true);

        assertTrue(result.isAvailable());
    }

    @Test
    void updateRating_ShouldUpdateDriverRating() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver result = driverService.updateRating(ratingRequest);

        assertEquals(4.16666, result.getRate(), 0.001);
    }
}
