package com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IntrospectRequest {
    private String token;
}
