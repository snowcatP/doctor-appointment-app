package com.hhh.doctor_appointment_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBeans {

    @Bean
    public PasswordEncoder encoder (){
        return new BCryptPasswordEncoder();
    }

}
