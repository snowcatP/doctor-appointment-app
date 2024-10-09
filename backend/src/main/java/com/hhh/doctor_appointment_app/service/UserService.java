package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.request.PatientRequest.AddPatientRequest;
import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.Admin;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.mapper.UserMapper;
import com.hhh.doctor_appointment_app.repository.*;
import com.hhh.doctor_appointment_app.util.singleton.PasswordEncoderSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PatientMapper patientMapper;

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = UserMapper.toAdmin(request);

        var role = roleRepository.findByRoleName(UserRole.ADMIN);
        newAdmin.getProfile().setRole(role);

        return adminRepository.save(newAdmin);
    }

//    public Patient createPatient(UserCreateRequest request) {
//        if (checkUsernameExists(request.getEmail())) {
//            throw new UserException("User already exists");
//        }
//        Patient newPatient = UserMapper.toPatient(request);
//
//        var role = roleRepository.findByRoleName(UserRole.PATIENT);
//        newPatient.getProfile().setRole(role);
//
//        return patientRepository.save(newPatient);
//    }

    public ApiResponse<Object> createPatient(UserCreateRequest userCreateRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            if (checkUsernameExists(userCreateRequest.getEmail())) {
                throw new UserException("User already exists");
            }

            User user = new User();
            user.setFullname(userCreateRequest.getFirstName() + " " +  userCreateRequest.getLastName());
            user.setGender(userCreateRequest.getGender());
            user.setPhone(userCreateRequest.getPhone());
            user.setEmail(userCreateRequest.getEmail());
            user.setDateOfBirth(userCreateRequest.getDateOfBirth());
            user.setAddress(userCreateRequest.getAddress());

            Patient newPatient = new Patient();
            newPatient.setProfile(user);

            var role = roleRepository.findByRoleName(UserRole.PATIENT);
            newPatient.getProfile().setRole(role);

            patientRepository.saveAndFlush(newPatient);
            PatientResponse patientResponse = patientMapper.toResponse(newPatient);
            apiResponse.ok(patientResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }

    @PostAuthorize("returnObject.profile.username == authentication.name")
    public Admin getAdminProfile(Long id) {
        log.info("Get admin profile");
        return adminRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return UserMapper.toUserResponse(user);
    }

    private Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    private boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
