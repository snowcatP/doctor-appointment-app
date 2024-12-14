package com.hhh.doctor_appointment_app.tests.IntegrationTest;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.LogoutRequest;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.LogoutResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testLoginSuccess() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("admin@gmail.com")
                .password("Hello@123")
                .build();

        ResponseEntity<AuthenticationResponse> response = restTemplate
                .postForEntity("/api/auth/login", request, AuthenticationResponse.class);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.requireNonNull(response.getBody()).isAuthenticated();
    }

    @Test
    public void testLoginFailure() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("admin@gmail.com")
                .password("Hello@123456")
                .build();

        ResponseEntity<AuthenticationResponse> response = restTemplate
                .postForEntity("/api/auth/login", request, AuthenticationResponse.class);
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void testLogoutSuccess() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("admin@gmail.com")
                .password("Hello@123")
                .build();

        ResponseEntity<AuthenticationResponse> response = restTemplate
                .postForEntity("/api/auth/login", request, AuthenticationResponse.class);

        LogoutRequest logoutRequest = LogoutRequest
                .builder()
                .token(response.getBody().getToken())
                .build();

        ResponseEntity<LogoutResponse> logoutResponse = restTemplate
                .postForEntity("/api/auth/logout", logoutRequest, LogoutResponse.class);
        assert logoutResponse.getStatusCode().is2xxSuccessful();
    }


}
