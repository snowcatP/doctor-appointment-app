package com.hhh.doctor_appointment_app.service.SpecialtyService.Query.GetDetailSpecialty;

import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyMapper;
import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyWithDoctor;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialResponeWithDoctorList;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDetailSpecialtyQuery {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyWithDoctor specialtyMapper;
    public ApiResponse<?> getSpecialtyDetail(Long id){
        ApiResponse<SpecialResponeWithDoctorList> apiResponse = new ApiResponse<>();
        try {
            Specialty specialty = specialtyRepository.findById(id).orElseThrow(() -> new NotFoundException("Specialty Not Found"));
            // Update

            SpecialResponeWithDoctorList specialtyResponse = specialtyMapper.toResponse(specialty);
            specialtyResponse.setSpecialtyName(specialty.getSpecialtyName());
            specialtyResponse.setHeadDoctor(specialty.getHeadDoctor());
            List<DoctorResponse> doctorResponseList = new ArrayList<>();
            for(Doctor doctor: specialty.getDoctorList()){
                DoctorResponse response = new DoctorResponse();
                response.setId(doctor.getId());
                response.setFirstName(doctor.getProfile().getFirstName());
                response.setLastName(doctor.getProfile().getLastName());
                response.setFullName(doctor.getProfile().getFullName());
                response.setGender(doctor.getProfile().isGender());
                response.setPhone(doctor.getProfile().getPhone());
                response.setEmail(doctor.getProfile().getEmail());
                response.setDateOfBirth(doctor.getProfile().getDateOfBirth());
                response.setAddress(doctor.getProfile().getAddress());
                response.setSpecialty(doctor.getSpecialty());
                response.setAvatarFilePath(doctor.getProfile().getAvatarFilePath());
                doctorResponseList.add(response);
            }
            specialtyResponse.setDoctorList(doctorResponseList);
            apiResponse.ok(specialtyResponse);
            apiResponse.setMessage("Get Specialty's Information Successfully");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
