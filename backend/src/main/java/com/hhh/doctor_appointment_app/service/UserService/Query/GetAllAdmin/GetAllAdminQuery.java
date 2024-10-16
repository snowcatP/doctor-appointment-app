package com.hhh.doctor_appointment_app.service.UserService.Query.GetAllAdmin;

import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetAllAdminQuery {
    @Autowired
    private AdminRepository adminRepository;
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }
}
