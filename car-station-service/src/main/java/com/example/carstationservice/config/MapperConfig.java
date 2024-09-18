package com.example.carstationservice.config;

import com.example.carstationservice.mapper.CarMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public CarMapper carMapper(){
        return CarMapper.INSTANCE;
    }
}
