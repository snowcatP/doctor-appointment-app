package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String email);
    Optional<User> findByUsername(String username);
}
