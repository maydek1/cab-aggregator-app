package com.software.modsen.carstationservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.carstationservice.CarStationServiceApplication;
import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.model.Car;
import com.software.modsen.carstationservice.repository.CarRepository;
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

@SpringBootTest(classes = CarStationServiceApplication.class)
@AutoConfigureMockMvc
public class CarIntegrationTest extends DataBaseContainerConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        carRepository.deleteAll();
    }

    private Car createTestCar(String model, String number) {
        Car car = new Car();
        car.setModel(model);
        car.setNumber(number);
        return carRepository.save(car);
    }

    private CarRequest createTestCarRequest() {
        CarRequest carRequest = new CarRequest();
        carRequest.setModel("Toyota");
        carRequest.setNumber("ABC-123");
        return carRequest;
    }

    @Test
    @SneakyThrows
    public void testCreateCar() {
        CarRequest carRequest = createTestCarRequest();

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Toyota"))
                .andExpect(jsonPath("$.number").value("ABC-123"));
    }

    @Test
    @SneakyThrows
    public void testGetCarById() {
        Car car = createTestCar("Toyota", "ABC-123");

        mockMvc.perform(get("/api/v1/cars/{id}", car.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Toyota"))
                .andExpect(jsonPath("$.number").value("ABC-123"));
    }

    @Test
    @SneakyThrows
    public void testUpdateCar() {
        Car car = createTestCar("Toyota", "ABC-123");

        CarRequest updateRequest = createTestCarRequest();
        updateRequest.setModel("Honda");
        updateRequest.setNumber("XYZ-987");

        mockMvc.perform(put("/api/v1/cars/{id}", car.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Honda"))
                .andExpect(jsonPath("$.number").value("XYZ-987"));
    }

    @Test
    @SneakyThrows
    public void testDeleteCar() {
        Car car = createTestCar("Toyota", "ABC-123");

        mockMvc.perform(delete("/api/v1/cars/{id}", car.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/cars/{id}", car.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testGetAllCars() {
        createTestCar("Toyota", "ABC-123");
        createTestCar("Honda", "XYZ-987");

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(2))
                .andExpect(jsonPath("$.cars[0].model").value("Honda"))
                .andExpect(jsonPath("$.cars[1].model").value("Toyota"));
    }
}
