package com.software.modsen.driverservice.mapper;

import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.model.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverResponse driverToDriverResponse(Driver driver);

    Driver driverRequestToDriver(DriverRequest driverRequest);
}
