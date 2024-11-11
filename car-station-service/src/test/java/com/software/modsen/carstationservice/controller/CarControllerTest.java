package com.software.modsen.carstationservice.controller;

import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.mapper.CarMapper;
import com.software.modsen.carstationservice.model.Car;
import com.software.modsen.carstationservice.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    private Car car;
    private CarResponse carResponse;
    private CarRequest carRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        car = new Car();
        car.setId(1L);
        car.setNumber("ABC123");

        carRequest = new CarRequest();
        carRequest.setNumber("XYZ789");

        carResponse = new CarResponse();
        carResponse.setId(1L);
        carResponse.setNumber("ABC123");
    }

    @Test
    void getCarById_ShouldReturnCarResponse_WhenCarExists() throws Exception {
        when(carService.getCarById(1L)).thenReturn(car);
        when(carMapper.carToCarResponse(car)).thenReturn(carResponse);

        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("ABC123"));

        verify(carService, times(1)).getCarById(1L);
    }

    @Test
    void createCar_ShouldReturnCreatedCar_WhenValidRequest() throws Exception {
        when(carService.createCar(any(CarRequest.class))).thenReturn(car);
        when(carMapper.carToCarResponse(car)).thenReturn(carResponse);

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"XYZ789\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("ABC123"));

        verify(carService, times(1)).createCar(any(CarRequest.class));
    }

    @Test
    void updateCar_ShouldReturnUpdatedCar_WhenCarExists() throws Exception {
        when(carService.updateCar(eq(1L), any(CarRequest.class))).thenReturn(car);
        when(carMapper.carToCarResponse(car)).thenReturn(carResponse);

        mockMvc.perform(put("/api/v1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"XYZ789\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("ABC123"));

        verify(carService, times(1)).updateCar(eq(1L), any(CarRequest.class));
    }

    @Test
    void deleteCarById_ShouldReturnDeletedCar_WhenCarExists() throws Exception {
        when(carService.deleteCarById(1L)).thenReturn(car);
        when(carMapper.carToCarResponse(car)).thenReturn(carResponse);

        mockMvc.perform(delete("/api/v1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("ABC123"));

        verify(carService, times(1)).deleteCarById(1L);
    }

    @Test
    void getAllCars_ShouldReturnSetOfCars() throws Exception {
        when(carService.getAllCars()).thenReturn(List.of(car));
        when(carMapper.carToCarResponse(car)).thenReturn(carResponse);


        mockMvc.perform(get("/api/v1/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cars[0].id").value(1L))
                .andExpect(jsonPath("$.cars[0].number").value("ABC123"));

        verify(carService, times(1)).getAllCars();
    }
}
