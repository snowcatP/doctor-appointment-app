package com.hhh.doctor_appointment_app.service.UserService.Query.GetAdminProfile;

import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

@Service
public class GetAdminProfileQuery {
    @Autowired
    private AdminRepository adminRepository;
    @PostAuthorize("returnObject.profile.email == authentication.name")
    public Admin getAdminProfile(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
