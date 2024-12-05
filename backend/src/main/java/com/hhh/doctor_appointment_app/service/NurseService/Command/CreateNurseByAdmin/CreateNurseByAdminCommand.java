package com.hhh.doctor_appointment_app.service.NurseService.Command.CreateNurseByAdmin;
import com.hhh.doctor_appointment_app.dto.mapper.NurseMapper;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.NurseRequest.AddNurseRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.NurseResponse.NurseResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Nurse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.NurseRepository;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CreateNurseByAdminCommand {
    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private NurseMapper nurseMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ApiResponse<Object> addNurseByAdmin(MultipartFile file, AddNurseRequest addNurseRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            //Check file has null ?
            if (file != null) {
                // Upload file to Firebase Storage if file not null
                String fileUrl = firebaseStorageService.uploadFile(file);
                addNurseRequest.setAvatarFilePath(fileUrl);
            }

            User user = new User();
            user.setFirstName(addNurseRequest.getFirstName());
            user.setLastName(addNurseRequest.getLastName());
            user.setFullName(user.getFullName() + " " + user.getLastName());
            user.setGender(addNurseRequest.isGender());
            user.setPhone(addNurseRequest.getPhone());
            user.setEmail(addNurseRequest.getEmail());
            user.setDateOfBirth(addNurseRequest.getDateOfBirth());
            user.setAddress(addNurseRequest.getAddress());
            user.setAvatarFilePath(addNurseRequest.getAvatarFilePath());

            Nurse newNurse = new Nurse();
            newNurse.setProfile(user);
            newNurse.getProfile().setPassword(passwordEncoder.encode(addNurseRequest.getPassword()));
            newNurse.getProfile().setActive(true);

            var role = roleRepository.findByRoleName(UserRole.NURSE);
            newNurse.getProfile().setRole(role);

            boolean isDuplicate = nurseRepository.existsByProfile_Email(newNurse.getProfile().getEmail());
            if(isDuplicate){
                apiResponse.setMessage("Nurse already exist in the system");
                apiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return apiResponse;
            }

            nurseRepository.saveAndFlush(newNurse);
            NurseResponse nurseResponse = nurseMapper.toResponse(newNurse);
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Nurse Created Successfully");
            apiResponse.setData(nurseResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }
}
