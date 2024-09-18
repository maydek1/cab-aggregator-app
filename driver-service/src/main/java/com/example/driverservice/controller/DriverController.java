package com.example.driverservice.controller;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.dto.response.DriverSetResponse;
import com.example.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

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
}
