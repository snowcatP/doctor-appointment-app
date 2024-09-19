package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<User_Role,Long> {
}
