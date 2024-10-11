package com.hhh.doctor_appointment_app.service;
import com.hhh.doctor_appointment_app.dto.mapper.UserMapper;
import com.hhh.doctor_appointment_app.dto.request.UserRequest.*;
import com.hhh.doctor_appointment_app.dto.response.UserResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.exception.UserException;
import com.hhh.doctor_appointment_app.repository.*;
import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public Admin createAdmin(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Admin newAdmin = userMapper.toAdmin(request);
        newAdmin.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newAdmin.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.ADMIN);
        newAdmin.getProfile().setRole(role);

        return adminRepository.save(newAdmin);
    }

    public Patient createPatient(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Patient newPatient = userMapper.toPatient(request);
        newPatient.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newPatient.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.PATIENT);
        newPatient.getProfile().setRole(role);

        return patientRepository.save(newPatient);
    }

    public Doctor createDoctor(UserCreateRequest request) {
        if (checkUsernameExists(request.getEmail())) {
            throw new UserException("User already exists");
        }
        Doctor newDoctor = userMapper.toDoctor(request);
        newDoctor.getProfile().setPassword(passwordEncoder.encode(request.getPassword()));
        newDoctor.getProfile().setActive(true);

        var role = roleRepository.findByRoleName(UserRole.DOCTOR);
        newDoctor.getProfile().setRole(role);

        return doctorRepository.save(newDoctor);
    }

    @PostAuthorize("returnObject.profile.email == authentication.name")
    public Admin getAdminProfile(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toUserResponse(user);
    }

    public User updateUserPassword(UserUpdatePasswordRequest request) {
        if (!checkRightUser(request.getEmail())) {
            throw new UserException("Username does not match");
        }
        var user = findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getPassword()
                .equals(passwordEncoder.encode(request.getOldPassword()))){
            throw new UserException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);

    }

    public User updateUserProfile(UserUpdateProfileRequest request) {
        if (!checkRightUser(request.getEmail())) {
            throw new UserException("Username does not match");
        }

        var user = findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.isGender());
        user.setPhone(request.getPhone());

        return userRepository.save(user);
    }

    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean checkUsernameExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean checkRightUser(String requestEmail) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        return email.equals(requestEmail);
    }

    public String forgotPassword(UserForgotPasswordRequest request) {
//        if (!checkUsernameExists(request.getEmail())){
//            return false;
//        }
        emailService.sendChangePasswordEmail(request.getEmail());

        return "Send email successfully, please check your email to reset password";
    }

    public String changeUserPassword(String token, UserChangePasswordRequest request)
            throws ParseException, JOSEException {
        String email = emailService.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!request.getNewPassword().equals(user.getPassword())) {
            return "Passwords do not match";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Reset password successfully";
    }
}
