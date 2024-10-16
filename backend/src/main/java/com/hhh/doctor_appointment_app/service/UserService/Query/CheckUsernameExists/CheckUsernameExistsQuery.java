package com.hhh.doctor_appointment_app.service.UserService.Query.CheckUsernameExists;

import com.hhh.doctor_appointment_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckUsernameExistsQuery {
    @Autowired
    private UserRepository userRepository;
    public boolean checkUsernameExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
