package com.example.driverservice.config;

import com.example.driverservice.mapper.DriverMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public DriverMapper driverMapper(){
        return DriverMapper.INSTANCE;
    }
}
