package com.hhh.doctor_appointment_app.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvalidatedToken {
    @Id
    private String id;
    private Date expiryTime;
}
