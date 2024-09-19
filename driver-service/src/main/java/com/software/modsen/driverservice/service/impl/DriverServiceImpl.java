package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.response.DriverSetResponse;
import com.software.modsen.driverservice.exceptions.DriverNotFoundException;
import com.software.modsen.driverservice.exceptions.EmailAlreadyExistException;
import com.software.modsen.driverservice.exceptions.PhoneAlreadyExistException;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.software.modsen.driverservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private DriverRepository driverRepository;
    private DriverMapper driverMapper;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper){
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverResponse getDriverById(Long id) {
        return getOrElseThrow(id);
    }

    @Override
    public DriverResponse deleteDriverById(Long id) {
        DriverResponse driverResponse = getOrElseThrow(id);
        driverRepository.deleteById(id);
        return driverResponse;
    }

    @Override
    public DriverResponse updateDriver(Long id, DriverRequest driverRequest) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

        checkEmailToExist(driver.getEmail(), driverRequest.getEmail());
        checkPhoneToExist(driver.getPhone(), driverRequest.getPhone());

        Driver driver1 = driverMapper.driverRequestToDriver(driverRequest);
        driver1.setId(driver.getId());
        return driverMapper.driverToDriverResponse(driverRepository.save(driver1));
    }

    private void checkEmailToExist(String currentEmail, String newEmail) {
        if (!Objects.equals(currentEmail, newEmail)) {
            if (driverRepository.existsByEmail(newEmail)){
                throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, newEmail));
            }
        }
    }

    private void checkPhoneToExist(String currentPhone, String newPhone) {
        if (!Objects.equals(currentPhone, newPhone)) {
            if (driverRepository.existsByPhone(newPhone)){
                throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, newPhone));
            }
        }
    }

    @Override
    public DriverResponse createDriver(DriverRequest driverRequest) {

        if (driverRepository.existsByEmail(driverRequest.getEmail())) {
            throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, driverRequest.getEmail()));
        }
        if (driverRepository.existsByPhone(driverRequest.getPhone())) {
            throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, driverRequest.getPhone()));
        }

        Driver driver = driverMapper.driverRequestToDriver(driverRequest);
        driverRepository.save(driver);
        return driverMapper.driverToDriverResponse(driver);
    }

    private DriverResponse getOrElseThrow(Long id){
        return driverMapper.driverToDriverResponse(driverRepository.findById(id)
                .orElseThrow(()-> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, id))));
    }

    @Override
    public DriverSetResponse getAllDrivers(){
        Set<DriverResponse> drivers = driverRepository.findAll()
                .stream()
                .map(driverMapper::driverToDriverResponse)
                .collect(Collectors.toSet());
        return new DriverSetResponse(drivers);
    }
}
