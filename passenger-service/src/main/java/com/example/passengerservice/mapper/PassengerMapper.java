package com.example.passengerservice.mapper;

import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerMapper {
    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);
    PassengerResponse passengerToPassengerResponse(Passenger passenger);


}
