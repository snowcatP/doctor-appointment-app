package com.hhh.doctor_appointment_app.dto.request.authenticationRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogoutRequest {
    private String token;
}