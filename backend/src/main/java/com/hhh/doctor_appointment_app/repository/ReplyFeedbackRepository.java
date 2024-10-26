package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.ReplyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyFeedbackRepository extends JpaRepository<ReplyFeedback, Long> {
    List<ReplyFeedback> getAllByFeedback_Id(Long feedback_Id);
}
