package com.hhh.doctor_appointment_app.dto.response.AuthResponse;

import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private boolean isAuthenticated;
    private String token;
    private UserResponse user;
}
