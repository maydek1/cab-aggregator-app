package com.software.modsen.carstationservice.mapper;


import com.software.modsen.carstationservice.dto.request.CarRequest;
import com.software.modsen.carstationservice.dto.response.CarResponse;
import com.software.modsen.carstationservice.model.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarResponse carToCarResponse(Car car);

    Car carRequestToCar(CarRequest carRequest);
}
