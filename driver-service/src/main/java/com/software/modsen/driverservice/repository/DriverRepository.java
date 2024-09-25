package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Driver findFirstByAvailableIs(boolean available);
}
