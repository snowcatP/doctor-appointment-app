package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@Table(name = "admin")
public class Admin extends User{
}
