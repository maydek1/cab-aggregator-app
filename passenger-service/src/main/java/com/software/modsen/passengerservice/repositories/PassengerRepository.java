package com.software.modsen.passengerservice.repositories;

import com.software.modsen.passengerservice.model.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
