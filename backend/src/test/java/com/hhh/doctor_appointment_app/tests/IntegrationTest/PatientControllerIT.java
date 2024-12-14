package com.hhh.doctor_appointment_app.tests.IntegrationTest;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.EditPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.Objects;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testUpdateProfilePatient() {
        Patient patient = patientRepository.findAll().get(0);

        EditPatientRequest editPatientRequest = EditPatientRequest.builder()
                .firstName(patient.getProfile().getFirstName())
                .lastName(patient.getProfile().getLastName())
                .address("Test address")
                .phone("5555555555")
                .email(patient.getProfile().getEmail())
                .gender(true)
                .dateOfBirth(new Date())
                .avatarFilePath(patient.getProfile().getAvatarFilePath())
                .build();

        String token = login(patient.getProfile().getEmail(), patient.getProfile().getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Convert editPatientRequest to form-data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("firstName", editPatientRequest.getFirstName());
        formData.add("lastName", editPatientRequest.getLastName());
        formData.add("address", editPatientRequest.getAddress());
        formData.add("phone", editPatientRequest.getPhone());
        formData.add("email", editPatientRequest.getEmail());
        formData.add("gender", String.valueOf(editPatientRequest.isGender()));
        formData.add("avatarFilePath", editPatientRequest.getAvatarFilePath());

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(
                        "/api/patient/edit-patient/" + patient.getId(),
                        HttpMethod.PUT,
                        entity,
                        Object.class);

        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testGetPatientProfile() {
        Patient patient = patientRepository.findAll().get(0);

        String token = login(patient.getProfile().getEmail(), patient.getProfile().getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(
                        "/api/patient/detail/" + patient.getId(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ApiResponse.class);
        assert Objects.requireNonNull(
                response.getBody()).getMessage().equals("Get Patient's Information Successfully");
    }

    private String login(String username, String password) {
        var response = restTemplate
                .postForObject("/api/auth/login",
                        AuthenticationRequest.builder().email(username).password("Hello@123").build(),
                        AuthenticationResponse.class);
        return response.getToken();
    }
}
