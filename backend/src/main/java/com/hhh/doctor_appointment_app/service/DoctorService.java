package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    public PageResponse<List<DoctorResponse>> getDoctorsWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Doctor> doctorPage = doctorRepository.getDoctorsWithPage(pageable);

        //Convert entities to responses
        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(doctor -> {
                    DoctorResponse response = new DoctorResponse();
                    response.setId(doctor.getId());
                    response.setFullname(doctor.getProfile().getFullname());
                    response.setGender(doctor.getProfile().isGender());
                    response.setPhone(doctor.getProfile().getPhone());
                    response.setEmail(doctor.getProfile().getEmail());
                    response.setDateOfBirth(doctor.getProfile().getDateOfBirth());
                    response.setAddress(doctor.getProfile().getAddress());
                    response.setSpecialty(doctor.getSpecialty());
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

    public ApiResponse<Object> addDoctor(AddDoctorRequest addDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Doctor newDoctor = new Doctor();
            newDoctor.getProfile().setFullname(addDoctorRequest.getFullname());
            newDoctor.getProfile().setGender(addDoctorRequest.isGender());
            newDoctor.getProfile().setPhone(addDoctorRequest.getPhone());
            newDoctor.getProfile().setEmail(addDoctorRequest.getEmail());
            newDoctor.getProfile().setDateOfBirth(addDoctorRequest.getDateOfBirth());
            newDoctor.getProfile().setAddress(addDoctorRequest.getAddress());
            newDoctor.setSpecialty(addDoctorRequest.getSpecialty());

            boolean isDuplicate = doctorRepository.existsByProfile_Email(newDoctor.getProfile().getEmail());
            if(isDuplicate){
                apiResponse.duplicatedCode();
                return apiResponse;
            }

            doctorRepository.saveAndFlush(newDoctor);
            DoctorResponse doctorResponse = doctorMapper.toResponse(newDoctor);
            apiResponse.ok(doctorResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }

    public ApiResponse<Object> editDoctor(Long id,EditDoctorRequest editDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Doctor existingDoctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            // Kiểm tra xem email trong edit request có khác với email của bác sĩ hiện tại hay không
            if (!editDoctorRequest.getEmail().equals(existingDoctor.getProfile().getEmail())) {
                // Nếu khác, kiểm tra xem email này có tồn tại trong hệ thống hay không (ngoại trừ bác sĩ hiện tại)
                boolean isDuplicate = doctorRepository.existsByProfile_Email(editDoctorRequest.getEmail());
                if (isDuplicate) {
                    // Nếu email đã tồn tại, trả về lỗi duplicate
                    apiResponse.duplicatedCode();
                    return apiResponse;
                }
            }

            existingDoctor.getProfile().setFullname(editDoctorRequest.getFullname());
            existingDoctor.getProfile().setGender(editDoctorRequest.isGender());
            existingDoctor.getProfile().setPhone(editDoctorRequest.getPhone());
            existingDoctor.getProfile().setEmail(editDoctorRequest.getEmail());
            existingDoctor.getProfile().setDateOfBirth(editDoctorRequest.getDateOfBirth());
            existingDoctor.getProfile().setAddress(editDoctorRequest.getAddress());
            existingDoctor.setSpecialty(existingDoctor.getSpecialty());

            doctorRepository.saveAndFlush(existingDoctor);

            DoctorResponse doctorResponse = doctorMapper.toResponse(existingDoctor);
            apiResponse.ok(doctorResponse);
        } catch (ApplicationException ex) {
            apiResponse.setStatusCode("400");
            apiResponse.setMessage("Failed to update");
        }
        return apiResponse;
    }

    public ApiResponse<?> deleteByIdDoctor(Long id){
        ApiResponse<DoctorResponse> apiResponse = new ApiResponse<>();

        try {
            Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor Not Found"));
            // Update
            doctorRepository.deleteById(doctor.getId());
            apiResponse.ok();
            apiResponse.setMessage("Doctor successfully deleted");
        }catch(Exception ex){
            apiResponse.setStatusCode("500");
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }

    public ApiResponse<?> getDoctorDetail(Long id){
        ApiResponse<DoctorResponse> apiResponse = new ApiResponse<>();
        try {
            Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor Not Found"));
            // Update
            DoctorResponse doctorResponse = doctorMapper.toResponse(doctor);
            apiResponse.ok(doctorResponse);
            apiResponse.setMessage("Get Doctor's Information Successfully");
        }catch(Exception ex){
            apiResponse.setStatusCode("500");
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
