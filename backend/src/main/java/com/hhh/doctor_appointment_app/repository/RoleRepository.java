package com.hhh.doctor_appointment_app.repository;

import com.hhh.doctor_appointment_app.entity.Role;
import com.hhh.doctor_appointment_app.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(UserRole roleName);
    boolean existsByRoleName(UserRole roleName);
}
