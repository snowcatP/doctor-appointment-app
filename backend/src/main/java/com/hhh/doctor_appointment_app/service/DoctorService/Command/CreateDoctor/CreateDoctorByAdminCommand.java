package com.hhh.doctor_appointment_app.service.DoctorService.Command.CreateDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CreateDoctorByAdminCommand {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ApiResponse<Object> addDoctor(MultipartFile file,AddDoctorRequest addDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            //Check file has null ?
            if (!file.isEmpty()) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                addDoctorRequest.setAvatarFilePath(fileUrl);
            }

            Specialty specialty = specialtyRepository.findById(addDoctorRequest.getSpecialtyID())
                    .orElseThrow(() -> new NotFoundException("Not found specialty"));
            User user = new User();
            user.setFirstName(addDoctorRequest.getFirstName());
            user.setLastName(addDoctorRequest.getLastName());
            user.setFullName(addDoctorRequest.getFirstName() + " " + addDoctorRequest.getLastName());
            user.setGender(addDoctorRequest.isGender());
            user.setPhone(addDoctorRequest.getPhone());
            user.setEmail(addDoctorRequest.getEmail());
            user.setDateOfBirth(addDoctorRequest.getDateOfBirth());
            user.setAddress(addDoctorRequest.getAddress());
            user.setAvatarFilePath(addDoctorRequest.getAvatarFilePath());

            Doctor newDoctor = new Doctor();
            newDoctor.setProfile(user);
            newDoctor.setSpecialty(specialty);

            newDoctor.getProfile().setPassword(passwordEncoder.encode(addDoctorRequest.getPassword()));
            newDoctor.getProfile().setActive(true);

            var role = roleRepository.findByRoleName(UserRole.DOCTOR);
            newDoctor.getProfile().setRole(role);

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
