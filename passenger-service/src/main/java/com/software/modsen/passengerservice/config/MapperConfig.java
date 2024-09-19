package com.software.modsen.passengerservice.config;

import com.software.modsen.passengerservice.mapper.PassengerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public PassengerMapper passengerMapper(){
        return PassengerMapper.INSTANCE;
    }
}
