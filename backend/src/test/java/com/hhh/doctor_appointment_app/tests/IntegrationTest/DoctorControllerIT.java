package com.hhh.doctor_appointment_app.tests.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.AuthenticationRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.SearchDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AuthResponse.AuthenticationResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.v129.profiler.model.Profile;
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

import javax.print.Doc;
import java.time.LocalDate;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctorControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSearchDoctorSuccess() {
        // Tạo request payload
        SearchDoctorRequest request = new SearchDoctorRequest();
        request.setKeyword("Ha");
        request.setGender(true);
        request.setSpecialtyId(Arrays.asList(1L, 2L));

        ResponseEntity<?> response = restTemplate.postForEntity(
                "/api/doctor/search?page=1&size=10",
                request,
                Object.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
    }

    @Test
    public void testSearchDoctorNoResults() {
        // Create request payload with keywords with no results
        SearchDoctorRequest request = new SearchDoctorRequest();
        request.setKeyword("aaaaaaaaaaaaaaaaaaa");
        request.setGender(null);
        request.setSpecialtyId(null);

        // Send request to endpoint `/search`
        ResponseEntity<PageResponse> response = restTemplate.postForEntity(
                "/api/doctor/search?page=1&size=10",
                request,
                PageResponse.class
        );

        assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetProfileOfDoctorSuccess() throws Exception{
        Doctor doctor = doctorRepository.findAll().get(0);
        Long doctorId = doctor.getId();

        // Send request to endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/detail/" + doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Check Response
        String responseContent = result.getResponse().getContentAsString();
        ApiResponse response = objectMapper.readValue(responseContent, ApiResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Get Doctor's Information Successfully", response.getMessage());

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data);
        assertEquals(doctor.getProfile().getFirstName(), data.get("firstName"));
        assertEquals(doctor.getProfile().getLastName(), data.get("lastName"));
        assertEquals(doctor.getProfile().isGender(), data.get("gender"));

    }

    @Test
    public void testGetProfileOfDoctorFailure() throws Exception {
        //use ID do not exist
        Long invalidDoctorId = 9999L;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/detail/" + invalidDoctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ApiResponse response = objectMapper.readValue(responseContent, ApiResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("Doctor Not Found", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testGetTop10DoctorSuccess() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/top-rating")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        PageResponse response = objectMapper.readValue(responseContent, PageResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());

        List<Map<String, Object>> topDoctors = (List<Map<String, Object>>) response.getData();
        assertEquals(topDoctors.size(), topDoctors.size());

        for (int i = 0; i < topDoctors.size(); i++) {
            Map<String, Object> doctor = topDoctors.get(i);
            assertNotNull(doctor.get("id"));
            assertNotNull(doctor.get("firstName"));
            assertNotNull(doctor.get("lastName"));
            assertNotNull(doctor.get("averageRating"));
        }
    }
}

