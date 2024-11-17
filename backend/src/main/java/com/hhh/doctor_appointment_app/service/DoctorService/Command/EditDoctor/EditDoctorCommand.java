package com.hhh.doctor_appointment_app.service.DoctorService.Command.EditDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EditDoctorCommand {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ApiResponse<Object> editDoctor(Long id, EditDoctorRequest editDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Specialty specialty = specialtyRepository.findById(editDoctorRequest.getSpecialtyID())
                    .orElseThrow(() -> new NotFoundException("Not found specialty"));

            Doctor existingDoctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Doctor Not Found"));

            existingDoctor.getProfile().setFirstName(editDoctorRequest.getFirstName());
            existingDoctor.getProfile().setLastName(editDoctorRequest.getLastName());
            existingDoctor.getProfile().setGender(editDoctorRequest.isGender());
            existingDoctor.getProfile().setPhone(editDoctorRequest.getPhone());
            existingDoctor.getProfile().setEmail(editDoctorRequest.getEmail());
            existingDoctor.getProfile().setDateOfBirth(editDoctorRequest.getDateOfBirth());
            existingDoctor.getProfile().setAddress(editDoctorRequest.getAddress());
            existingDoctor.setSpecialty(specialty);
            existingDoctor.getProfile().setAvatarFilePath(editDoctorRequest.getAvatarFilePath());

            doctorRepository.save(existingDoctor);

            DoctorResponse doctorResponse = doctorMapper.toResponse(existingDoctor);
            apiResponse.ok(doctorResponse);
        } catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch (ApplicationException ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Failed to update");
        }
        return apiResponse;
    }
}
