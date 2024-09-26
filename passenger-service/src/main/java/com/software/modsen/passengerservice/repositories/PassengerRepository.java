package com.software.modsen.passengerservice.repositories;

import com.software.modsen.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
