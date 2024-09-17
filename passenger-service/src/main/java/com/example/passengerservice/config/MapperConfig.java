package com.example.passengerservice.config;

import com.example.passengerservice.mapper.PassengerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public PassengerMapper passengerMapper(){
        return PassengerMapper.INSTANCE;
    }
}
