package com.software.modsen.passengerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Document(collection = "passengers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Passenger {
    @Id
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private BigDecimal money;
}
