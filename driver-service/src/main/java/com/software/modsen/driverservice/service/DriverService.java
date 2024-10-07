package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.client.CarClient;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.request.DriverRequest;
import com.software.modsen.driverservice.exceptions.CarNotFoundException;
import com.software.modsen.driverservice.exceptions.DriverNotFoundException;
import com.software.modsen.driverservice.exceptions.EmailAlreadyExistException;
import com.software.modsen.driverservice.exceptions.PhoneAlreadyExistException;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.model.Driver;
import com.software.modsen.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.software.modsen.driverservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final CarClient carClient;

    public Driver getDriverById(Long id) {
        return getOrElseThrow(id);
    }

    public Driver deleteDriverById(Long id) {
        Driver driver = getOrElseThrow(id);
        driverRepository.deleteById(id);
        return driver;
    }

    public Driver updateDriver(Long id, DriverRequest driverRequest) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

        checkEmailToExist(driver.getEmail(), driverRequest.getEmail());
        checkPhoneToExist(driver.getPhone(), driverRequest.getPhone());

        Driver driver1 = driverMapper.driverRequestToDriver(driverRequest);
        driver1.setId(driver.getId());
        return driverRepository.save(driver1);
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

    public Driver createDriver(DriverRequest driverRequest) {
        if (driverRequest.getCarId() != null)
            if (carClient.getCarById(driverRequest.getCarId()) == null) throw new CarNotFoundException(String.format(CAR_NOT_FOUND, driverRequest.getCarId()));

        if (driverRepository.existsByEmail(driverRequest.getEmail())) {
            throw new EmailAlreadyExistException(String.format(EMAIL_ALREADY_EXIST, driverRequest.getEmail()));
        }
        if (driverRepository.existsByPhone(driverRequest.getPhone())) {
            throw new PhoneAlreadyExistException(String.format(PHONE_ALREADY_EXIST, driverRequest.getPhone()));
        }
        Driver driver = driverMapper.driverRequestToDriver(driverRequest);
        driver.setAvailable(true);
        return driverRepository.save(driver);
    }

    private Driver getOrElseThrow(Long id){
        return driverRepository.findById(id)
                .orElseThrow(()-> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, id)));
    }

    public List<Driver> getAllDrivers(){
        return driverRepository.findAll();
    }

    public Driver getAvailableDriver() {
        return driverRepository.findFirstByAvailableAndCarIdNotNull(true);
    }

    public Driver toggleAvailable(Long id, boolean available){
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, id)));
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    public Driver updateRating(DriverRatingRequest driverRatingRequest) {
        Driver driver = driverRepository.findById(driverRatingRequest.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(String.format(DRIVER_NOT_FOUND,
                        driverRatingRequest.getDriverId())));
        driver.setRate(setRating(driver.getRatingCount(), driverRatingRequest.getRate(), driver.getRate()));
        driver.setRatingCount(driver.getRatingCount()+1);
        return driverRepository.save(driver);
    }

    public double setRating(int ratingCount, double rate, double oldRate){
        rate = (rate + oldRate) / (ratingCount+1);
        if (rate > 5) return 5;
        return rate;
    }
}
