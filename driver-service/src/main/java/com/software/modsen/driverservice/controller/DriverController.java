package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.*;
import com.software.modsen.driverservice.service.DriverService;
import com.software.modsen.driverservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final RideService rideService;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@RequestBody DriverRequest driverRequest) {
        return new ResponseEntity<>(driverService.createDriver(driverRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                                       @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.ok(driverService.updateDriver(id, driverRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DriverResponse> deleteDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.deleteDriverById(id));
    }

    @GetMapping
    public ResponseEntity<DriverSetResponse> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/free")
    public ResponseEntity<DriverResponse> getFreeDriver() {
        return ResponseEntity.ok(driverService.getFreeDriver());
    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<RideResponseSet> getRideToConfirm(@PathVariable Long id){
        return ResponseEntity.ok(rideService.getRideToConfirm(id));
    }

    @PostMapping("/accept/{id}")
    ResponseEntity<RideResponse> acceptRide(@PathVariable Long id){
        return ResponseEntity.ok(rideService.acceptRide(id));
    }

    @PostMapping("/reject/{id}")
    ResponseEntity<RideResponse> rejectRide(@PathVariable Long id){
        return ResponseEntity.ok(rideService.rejectRide(id));

    }

    @PostMapping("/completed/{id}")
    ResponseEntity<RideResponse> completedRide(@PathVariable Long id){
        return ResponseEntity.ok(rideService.completedRide(id));
    }

    @PostMapping("/driver/rating")
    ResponseEntity<DriverResponse> updateRating(@RequestBody DriverRatingRequest driverRatingRequest){
        return ResponseEntity.ok(driverService.updateRating(driverRatingRequest));
    }
}