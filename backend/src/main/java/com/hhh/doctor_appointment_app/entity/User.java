package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private boolean gender;
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;
    private Date dateOfBirth;
    private String address;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean isActive = true;
}
