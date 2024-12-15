package com.hhh.doctor_appointment_app.tests.IntegrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.ReferenceCodeRequest;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Test
    public void testGetListAppointmentOfDoctorSuccess() throws Exception {
        var doctor = doctorRepository.findAll().get(0);
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email(doctor.getProfile().getEmail())
                .password("Hello@123")
                .build();

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(loginResponse, AuthenticationResponse.class);
        String token = authResponse.getToken();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/list/doctor?page=1&size=10")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        PageResponse response = objectMapper.readValue(responseContent, PageResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetListAppointmentOfPatientSuccess() throws Exception {
        var doctor = doctorRepository.findAll().get(0);
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email(doctor.getProfile().getEmail())
                .password("Hello@123")
                .build();

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(loginResponse, AuthenticationResponse.class);
        String token = authResponse.getToken();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/list/patient?page=1&size=10")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        PageResponse response = objectMapper.readValue(responseContent, PageResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetAppointmentOfPatientByPatientID() throws Exception {

        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("admin@gmail.com")
                .password("Hello@123")
                .build();

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Patient patient = patientRepository.findAll().get(0);
        Long patientId = patient.getId();

        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(loginResponse, AuthenticationResponse.class);
        String token = authResponse.getToken();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointment/list/patient/" + patientId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        PageResponse response = objectMapper.readValue(responseContent, PageResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetAppointmentByCode() throws Exception {
        var apps = appointmentRepository.findAll();
        Appointment appointment = appointmentRepository.findAll().get(apps.size() - 1);
        String appointmentCode = appointment.getReferenceCode();

        ReferenceCodeRequest referenceCodeRequest = new ReferenceCodeRequest();
        referenceCodeRequest.setReferenceCode(appointmentCode);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/appointment/search/reference-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(referenceCodeRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ApiResponse<AppointmentResponse> response = objectMapper.readValue(responseContent, new TypeReference<>() {});

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Get Appointment's Information Successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(appointment.getFullName(), response.getData().getFullName());
    }



}
