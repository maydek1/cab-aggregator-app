package com.example.driverservice.mapper;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DriverMapper {
    DriverMapper INSTANCE = Mappers.getMapper(DriverMapper.class);

    DriverResponse driverToDriverResponse(Driver driver);

    Driver driverRequestToDriver(DriverRequest driverRequest);
}
