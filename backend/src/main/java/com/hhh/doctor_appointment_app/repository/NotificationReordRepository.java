package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationReordRepository extends JpaRepository<Notification,Long> {
}
