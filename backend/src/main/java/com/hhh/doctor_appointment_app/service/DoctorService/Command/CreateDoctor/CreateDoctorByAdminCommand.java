package com.hhh.doctor_appointment_app.service.DoctorService.Command.CreateDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDoctorByAdminCommand {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    public ApiResponse<Object> addDoctor(AddDoctorRequest addDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Specialty specialty = specialtyRepository.findById(addDoctorRequest.getSpecialtyID())
                    .orElseThrow(() -> new NotFoundException("Not found specialty"));
            User user = new User();
            user.setFirstName(addDoctorRequest.getFirstName());
            user.setLastName(addDoctorRequest.getLastName());
            user.setGender(addDoctorRequest.isGender());
            user.setPhone(addDoctorRequest.getPhone());
            user.setEmail(addDoctorRequest.getEmail());
            user.setDateOfBirth(addDoctorRequest.getDateOfBirth());
            user.setAddress(addDoctorRequest.getAddress());

            Doctor newDoctor = new Doctor();
            newDoctor.setProfile(user);
            newDoctor.setSpecialty(specialty);

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
}
