package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String fullname;
    private boolean gender;
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;
    private Date dateOfBirth;
    private String address;
    private String username;
    private String password;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean isActive = true;
}
