package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.ChargeMoneyRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerController passengerController;

    private MockMvc mockMvc;

    private Passenger passenger;
    private PassengerResponse passengerResponse;
    private PassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();

        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@mail.com");
        passenger.setPhone("123456789");
        passenger.setMoney(BigDecimal.valueOf(100));

        passengerRequest = new PassengerRequest();
        passengerRequest.setEmail("test@mail.com");
        passengerRequest.setPhone("123456789");
        passengerRequest.setMoney(BigDecimal.valueOf(100));

        passengerResponse = new PassengerResponse();
        passengerResponse.setId(1L);
        passengerResponse.setEmail("test@mail.com");
        passengerResponse.setPhone("123456789");
        passengerResponse.setMoney(BigDecimal.valueOf(100));
    }

    @Test
    void getPassengerById_ShouldReturnPassengerResponse_WhenPassengerExists() throws Exception {
        when(passengerService.getPassengerById(1L)).thenReturn(passenger);
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(get("/api/v1/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@mail.com"));

        verify(passengerService, times(1)).getPassengerById(1L);
    }

    @Test
    void createPassenger_ShouldReturnCreatedPassenger_WhenValidRequest() throws Exception {
        when(passengerService.createPassenger(any(PassengerRequest.class))).thenReturn(passenger);
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@mail.com\", \"phone\":\"123456789\", \"money\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@mail.com"));

        verify(passengerService, times(1)).createPassenger(any(PassengerRequest.class));
    }

    @Test
    void updatePassenger_ShouldReturnUpdatedPassenger_WhenPassengerExists() throws Exception {
        when(passengerService.updatePassenger(eq(1L), any(PassengerRequest.class))).thenReturn(passenger);
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(put("/api/v1/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@mail.com\", \"phone\":\"123456789\", \"money\":150}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"));

        verify(passengerService, times(1)).updatePassenger(eq(1L), any(PassengerRequest.class));
    }

    @Test
    void deletePassengerById_ShouldReturnNoContent_WhenPassengerExists() throws Exception {
        when(passengerService.deletePassengerById(1L)).thenReturn(passenger);
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(delete("/api/v1/passengers/1"))
                .andExpect(status().isNoContent());

        verify(passengerService, times(1)).deletePassengerById(1L);
    }

    @Test
    void getAllPassengers_ShouldReturnSetOfPassengers() throws Exception {
        when(passengerService.getAllPassenger()).thenReturn(List.of(passenger));
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengers[0].id").value(1L))
                .andExpect(jsonPath("$.passengers[0].email").value("test@mail.com"));

        verify(passengerService, times(1)).getAllPassenger();
    }

    @Test
    void chargeMoney_ShouldReturnUpdatedPassengerResponse_WhenRequestIsValid() throws Exception {
        ChargeMoneyRequest chargeMoneyRequest = new ChargeMoneyRequest();
        chargeMoneyRequest.setPassengerId(1L);
        chargeMoneyRequest.setMoney(BigDecimal.valueOf(50));

        when(passengerService.chargeMoney(any(ChargeMoneyRequest.class))).thenReturn(passenger);
        when(passengerMapper.passengerToPassengerResponse(passenger)).thenReturn(passengerResponse);

        mockMvc.perform(post("/api/v1/passengers/money")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.money").value(100));

        verify(passengerService, times(1)).chargeMoney(any(ChargeMoneyRequest.class));
    }
}
