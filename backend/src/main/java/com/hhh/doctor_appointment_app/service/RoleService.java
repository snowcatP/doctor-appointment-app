package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.entity.Role;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByRoleName(UserRole role) {
        return roleRepository.findByRoleName(role);
    }
}
