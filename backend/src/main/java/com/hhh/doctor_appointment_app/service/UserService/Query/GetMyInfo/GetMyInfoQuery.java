package com.hhh.doctor_appointment_app.service.UserService.Query.GetMyInfo;

import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GetMyInfoQuery {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toUserResponse(user);
    }
}
