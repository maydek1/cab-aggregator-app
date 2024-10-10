package com.software.modsen.passengerservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.PassengerServiceApplication;
import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repositories.PassengerRepository;
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

@SpringBootTest(classes = PassengerServiceApplication.class)
@AutoConfigureMockMvc
public class PassengerIntegrationTest extends DataBaseContainerConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        passengerRepository.deleteAll();
    }

    private PassengerRequest createTestPassengerRequest(String firstName, String lastName, String phone, String email) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setFirstName(firstName);
        passengerRequest.setSecondName(lastName);
        passengerRequest.setEmail(email);
        passengerRequest.setPhone(phone);
        return passengerRequest;
    }


    private Passenger createTestPassenger(String firstName, String lastName, String phone, String email) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setSecondName(lastName);
        passenger.setEmail(email);
        passenger.setPhone(phone);
        passengerRepository.save(passenger);
        return passenger;
    }

    @Test
    @SneakyThrows
    public void testGetPassengerById() {
        Passenger passenger = createTestPassenger("Philip", "Kirkorov", "+375257435", "kir@gmail.com");

        mockMvc.perform(get("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kir@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Philip"));
    }

    @Test
    @SneakyThrows
    public void testGetAllPassengers() {
        Passenger passenger = createTestPassenger("Philip", "Kirkorov", "+375257435", "kir@gmail.com");
        Passenger passenger2 = createTestPassenger("Nikola", "Leps", "+375257467435", "nikola@gmail.com");

        mockMvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengers.length()").value(2));
    }

    @Test
    @SneakyThrows
    public void testCreatePassenger() {
        PassengerRequest passengerRequest = createTestPassengerRequest("Philip", "Kirkorov", "+375257435", "kir@gmail.com");

        mockMvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengers[0].email").value("kir@gmail.com"));

    }

    @Test
    @SneakyThrows
    public void testCreatePassengerWithEmailAlreadyExist() {
        PassengerRequest passengerRequest = createTestPassengerRequest("Philip", "Kirkorov", "+375257435", "nikola@gmail.com");
        Passenger passenger = createTestPassenger("Nikola", "Leps", "+375257467435", "nikola@gmail.com");
        mockMvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void testDeletePassengerById() {
        Passenger passenger = createTestPassenger("Nikola", "Leps", "+375257467435", "nikola@gmail.com");

        mockMvc.perform(delete("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testUpdatePassengerById() {
        Passenger passenger = createTestPassenger("Nikola", "Leps", "+375257467435", "nikola@gmail.com");
        PassengerRequest passengerRequest = createTestPassengerRequest("Philip", "Kirkorov", "+375257435", "kir@gmail.com");

        mockMvc.perform(put("/api/v1/passengers/{id}", passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kir@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Philip"));
    }

    @Test
    @SneakyThrows
    public void testUpdatePassengerByIdWhenEmailAlreadyExist() {
        Passenger passenger = createTestPassenger("Nikola", "Leps", "+375257467435", "nikola@gmail.com");
        Passenger passenger2 = createTestPassenger("Nikkola", "Leps", "+375257467435", "kir@gmail.com");

        PassengerRequest passengerRequest = createTestPassengerRequest("Philip", "Kirkorov", "+375257435", "kir@gmail.com");

        mockMvc.perform(put("/api/v1/passengers/{id}", passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isBadRequest());
    }
}