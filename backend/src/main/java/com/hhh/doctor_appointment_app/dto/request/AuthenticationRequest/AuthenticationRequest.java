package com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class AuthenticationRequest {
    private String email;
    private String password;
}
