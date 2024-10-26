package com.hhh.doctor_appointment_app.service.DoctorService.Query.SearchDoctors;

import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.SearchDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchDoctorsQuery {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;
    public PageResponse<List<DoctorResponse>> searchDoctorsAndPagination(SearchDoctorRequest searchDoctorRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        // Lấy danh sách chuyên ngành nếu có
        List<String> specialties = null;
        if (searchDoctorRequest.getSpecialty_Id() != null && !searchDoctorRequest.getSpecialty_Id().isEmpty()) {
            specialties = specialtyRepository.findAllById(searchDoctorRequest.getSpecialty_Id())
                    .stream()
                    .map(Specialty::getSpecialtyName)
                    .collect(Collectors.toList());
        }

        System.out.println(searchDoctorRequest.getGender());
        // Tìm kiếm với nhiều tham số
        Page<Doctor> doctorPage = doctorRepository.searchDoctors(
                searchDoctorRequest.getKeyword(),
                specialties,
                searchDoctorRequest.getGender(), // Có thể là null
                pageable
        );

        //Convert entities to responses
        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(doctor -> {
                    DoctorResponse response = new DoctorResponse();
                    response.setId(doctor.getId());
                    response.setFirstName(doctor.getProfile().getFirstName());
                    response.setLastName(doctor.getProfile().getLastName());
                    response.setGender(doctor.getProfile().isGender());
                    response.setPhone(doctor.getProfile().getPhone());
                    response.setEmail(doctor.getProfile().getEmail());
                    response.setDateOfBirth(doctor.getProfile().getDateOfBirth());
                    response.setAddress(doctor.getProfile().getAddress());
                    response.setSpecialty(doctor.getSpecialty());
                    response.setAvatarFilePath(doctor.getAvatarFilePath());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<DoctorResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(doctorResponses);
        double total = Math.ceil((double) doctorPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }
}
