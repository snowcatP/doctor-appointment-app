package com.hhh.doctor_appointment_app.mapper;

import com.hhh.doctor_appointment_app.dto.UserDto;
import com.hhh.doctor_appointment_app.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .fullname(userDto.getFullname())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .dateOfBirth(userDto.getDateOfBirth())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .username(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
}
