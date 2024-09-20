package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.UserDto;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.mapper.UserMapper;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User CreateUser(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(newUser);
    }
}
