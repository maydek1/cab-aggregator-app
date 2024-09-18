package com.example.driverservice.service;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.dto.response.DriverSetResponse;

public interface DriverService {
    DriverResponse getDriverById(Long id);
    DriverResponse deleteDriverById(Long id);
    DriverResponse updateDriver(Long id, DriverRequest driverRequest);
    DriverResponse createDriver(DriverRequest driverRequest);

    DriverSetResponse getAllDrivers();
}
