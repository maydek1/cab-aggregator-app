package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.client.RideClient;
import com.software.modsen.driverservice.dto.response.RideResponse;
import com.software.modsen.driverservice.dto.response.RideResponseSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideClient rideClient;
    private final DriverService driverService;

    public RideResponseSet getRideToConfirm(Long id){
        return rideClient.getRideToConfirm(id);
    }

    public RideResponse acceptRide(Long id){
        driverService.toggleAvailable(id, false);
        return rideClient.acceptRide(id);
    }

    public RideResponse rejectRide(Long id){
        return rideClient.rejectRide(id);
    }

    public RideResponse completedRide(Long id){
        driverService.toggleAvailable(id, true);
        return rideClient.completedRide(id);
    }


}
