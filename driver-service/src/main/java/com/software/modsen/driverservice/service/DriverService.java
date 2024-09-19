package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;

public interface DriverService {
    DriverResponse getDriverById(Long id);
    DriverResponse deleteDriverById(Long id);
    DriverResponse updateDriver(Long id, DriverRequest driverRequest);
    DriverResponse createDriver(DriverRequest driverRequest);

    DriverSetResponse getAllDrivers();
}
