package com.software.modsen.driverservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private String sex;
    private Long car_id;
    private boolean available;
    private int rate;
    private int ratingCount;
}
