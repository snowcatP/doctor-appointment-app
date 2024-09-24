package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;
import java.util.Set;

@MappedSuperclass
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email",  nullable = false, unique = true)
    private String email;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;
}
