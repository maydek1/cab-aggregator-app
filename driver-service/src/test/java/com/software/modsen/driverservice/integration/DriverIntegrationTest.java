package com.software.modsen.driverservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.driverservice.DriverServiceApplication;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DriverServiceApplication.class)
@AutoConfigureMockMvc
public class DriverIntegrationTest extends DataBaseContainerConfiguration {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        driverRepository.deleteAll();
    }

    private Driver createTestDriver(String email, String firstName) {
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setFirstName(firstName);
        return driverRepository.save(driver);
    }

    private Driver createTestDriver(String email, String firstName, String phone, Long carId) {
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setAvailable(true);
        driver.setPhone(phone);
        driver.setCarId(carId);
        driver.setFirstName(firstName);
        return driverRepository.save(driver);
    }

    private Driver createTestDriver(Long id, int rate, int countRating) {
        Driver driver = new Driver();
        driver.setId(id);
        driver.setRate(rate);
        driver.setRatingCount(countRating);
        return driverRepository.save(driver);
    }

    private DriverRequest createTestDriverRequest() {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setEmail("driver@gmail.com");
        driverRequest.setFirstName("Kirkorov");
        return driverRequest;
    }

    @Test
    @SneakyThrows
    public void testCreateDriver() {
        DriverRequest driverRequest = createTestDriverRequest();

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("driver@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Kirkorov"));
    }

    @Test
    @SneakyThrows
    public void testGetDriverById() {
        Driver driver = createTestDriver("driver@gmail.com", "Kirkorov");

        mockMvc.perform(get("/api/v1/drivers/{id}", driver.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("driver@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Kirkorov"));
    }

    @Test
    @SneakyThrows
    public void testUpdateDriver() {
        Driver driver = createTestDriver("driver@gmail.com", "Kirkorov", "+234", 5L);

        DriverRequest updateRequest = createTestDriverRequest();
        updateRequest.setEmail("mega@gmail.com");
        updateRequest.setFirstName("Leps");

        mockMvc.perform(put("/api/v1/drivers/{id}", driver.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mega@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Leps"));
    }

    @Test
    @SneakyThrows
    public void testDeleteDriver() {
        Driver driver = createTestDriver("driver@gmail.com", "Kirkorov");

        mockMvc.perform(delete("/api/v1/drivers/{id}", driver.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/drivers/{id}", driver.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testGetAllDrivers() {
        Driver driver = createTestDriver("driver@gmail.com", "Kirkorov");
        Driver driver2 = createTestDriver("driver2@gmail.com", "Kirkorov2", "+34566", 4L);

        mockMvc.perform(get("/api/v1/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drivers").isArray());

    }

    @Test
    @SneakyThrows
    public void testGetFreeDriver() {
        createTestDriver("driver@gmail.com", "Kirkorov", "+644664", 6L);

        mockMvc.perform(get("/api/v1/drivers/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("driver@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Kirkorov"));
    }

    @Test
    @SneakyThrows
    public void testGetUpdateRating() {
        createTestDriver(1L, 5, 1);
        Long driverId = driverRepository.findAll().get(0).getId();
        DriverRatingRequest driverRatingRequest = new DriverRatingRequest(1L, 3, driverId);

        mockMvc.perform(post("/api/v1/drivers/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRatingRequest)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(4))
                .andExpect(jsonPath("$.ratingCount").value(2));
    }

    @Test
    @SneakyThrows
    public void testToggleAvailableDriver() {
        createTestDriver("driver@gmail.com", "Kirkorov", "+644664", 6L);
        Long driverId = driverRepository.findAll().get(0).getId();

        mockMvc.perform(post("/api/v1/drivers/available/{id}/{available}", driverId, false))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.id").value(driverId));

    }


}
