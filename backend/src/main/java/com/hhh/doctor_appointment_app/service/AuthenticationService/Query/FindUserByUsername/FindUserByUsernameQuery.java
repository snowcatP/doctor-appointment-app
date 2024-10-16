package com.hhh.doctor_appointment_app.service.AuthenticationService.Query.FindUserByUsername;

import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class FindUserByUsernameQuery {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }
}
