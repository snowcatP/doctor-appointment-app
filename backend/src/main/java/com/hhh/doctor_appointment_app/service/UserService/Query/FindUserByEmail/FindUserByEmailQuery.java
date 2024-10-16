package com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail;

import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public class FindUserByEmailQuery {
    @Autowired
    private UserRepository userRepository;
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
