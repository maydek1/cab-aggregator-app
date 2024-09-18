package com.example.carstationservice.mapper;


import com.example.carstationservice.dto.request.CarRequest;
import com.example.carstationservice.dto.response.CarResponse;
import com.example.carstationservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarResponse carToCarResponse(Car car);

    Car carRequestToCar(CarRequest carRequest);
}
