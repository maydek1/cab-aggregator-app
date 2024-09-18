package com.example.driverservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String second_name;
    private String phone;
    private String email;
    private String sex;
    private Long car_id;
}
