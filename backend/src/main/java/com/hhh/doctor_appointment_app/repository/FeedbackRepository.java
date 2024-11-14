package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    List<Feedback> findAllByDoctor_Id(Long doctor_ID);

    Page<Feedback> findAllByDoctor_Profile_Email(String email, Pageable pageable);
}
