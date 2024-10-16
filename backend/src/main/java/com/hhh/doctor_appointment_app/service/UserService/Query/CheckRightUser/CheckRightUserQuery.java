package com.hhh.doctor_appointment_app.service.UserService.Query.CheckRightUser;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CheckRightUserQuery {
    public boolean checkRightUser(String requestEmail) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        return email.equals(requestEmail);
    }
}
