package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.controller.swagger.DriverApi;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController implements DriverApi {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.getDriverById(id)));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DRIVER')")
    public ResponseEntity<DriverResponse> createDriver(@RequestBody DriverRequest driverRequest) {
        return new ResponseEntity<>(driverMapper.driverToDriverResponse(driverService.createDriver(driverRequest)), HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                                       @RequestBody DriverRequest driverRequest) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.updateDriver(id, driverRequest)));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> deleteDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.deleteDriverById(id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<DriverSetResponse> getAllDrivers() {
        return ResponseEntity.ok(new DriverSetResponse(driverService.getAllDrivers()
                .stream()
                .map(driverMapper::driverToDriverResponse)
                .collect(Collectors.toSet())));
    }

    @Override
    @GetMapping("/free")
    @PreAuthorize("hasRole('ROLE_PASSENGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<DriverResponse> getFreeDriver() {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.getAvailableDriver()));
    }

    @Override
    @PostMapping("/rating")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PASSENGER')")
    public ResponseEntity<DriverResponse> updateRating(@RequestBody DriverRatingRequest driverRatingRequest) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.updateRating(driverRatingRequest)));
    }

    @Override
    @PostMapping("/available/{id}/{available}")
    public ResponseEntity<DriverResponse> toggleAvailable(@PathVariable Long id, @PathVariable boolean available) {
        return ResponseEntity.ok(driverMapper.driverToDriverResponse(driverService.toggleAvailable(id, available)));
    }
}
