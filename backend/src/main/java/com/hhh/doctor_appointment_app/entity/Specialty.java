package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialty")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "specialtyName")
    private String specialtyName;

    @OneToMany(mappedBy = "specialty",cascade = CascadeType.ALL)
    private List<Doctor> doctorList = new ArrayList<>();
}
