package com.hhh.doctor_appointment_app.dto.request.authenticationRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshTokenRequest {
    private String token;
}
