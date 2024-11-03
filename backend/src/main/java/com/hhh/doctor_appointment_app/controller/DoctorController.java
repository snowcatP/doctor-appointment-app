package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.AddDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.EditDoctorRequest;
import com.hhh.doctor_appointment_app.dto.request.DoctorRequest.SearchDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorBookingResponse;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.service.DoctorService.Command.CreateDoctor.CreateDoctorByAdminCommand;
import com.hhh.doctor_appointment_app.service.DoctorService.Command.DeleteDoctor.DeleteDoctorCommand;
import com.hhh.doctor_appointment_app.service.DoctorService.Command.EditDoctor.EditDoctorCommand;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.GetAllDoctorsForBooking.GetAllDoctorsForBookingQuery;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.GetDetailDoctor.GetDetailDoctorQuery;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.GetDoctorWithPage.GetDoctorWithPageQuery;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.GetTopTenRatingDoctor.GetTopTenRatingDoctorQuery;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.SearchDoctors.SearchDoctorsQuery;
import com.hhh.doctor_appointment_app.service.FirebaseStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/doctor")
@CrossOrigin("*")
public class DoctorController {
    @Autowired
    private GetDoctorWithPageQuery getDoctorsWithPage;

    @Autowired
    private CreateDoctorByAdminCommand createDoctorCommand;

    @Autowired
    private EditDoctorCommand editDoctorCommand;

    @Autowired
    private DeleteDoctorCommand deleteDoctorCommand;

    @Autowired
    private GetDetailDoctorQuery getDoctorDetail;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private SearchDoctorsQuery searchDoctorsQuery;

    @Autowired
    private GetTopTenRatingDoctorQuery getTopTenRatingDoctorQuery;
    @Autowired
    private GetAllDoctorsForBookingQuery getAllDoctorsForBookingQuery;

    @GetMapping("/list-doctor")
    public ResponseEntity<?> getDoctors(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(getDoctorsWithPage.getDoctorsWithPage(page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchDoctors(@RequestBody SearchDoctorRequest searchDoctorRequest, @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size){
        try{
            return new ResponseEntity<>(searchDoctorsQuery.searchDoctorsAndPagination(searchDoctorRequest,page, size), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping("/add-doctor")
    public ResponseEntity<?> createAndUploadFileDoctorByAdmin(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid AddDoctorRequest addDoctorRequest, // sử dụng ModelAttribute để bind dữ liệu
            BindingResult bindingResult) {

        ApiResponse<Object> apiResponse = new ApiResponse<>();

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Validation errors: " + errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse); // Trả về lỗi 400 cho validation
        }

        try {

            // Lưu hồ sơ
            apiResponse = createDoctorCommand.addDoctor(file,addDoctorRequest);

            // Kiểm tra xem email đã tồn tại trong hệ thống hay chưa
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == apiResponse.getStatusCode()) {
                apiResponse.setMessage("Doctor's Email already exists in the system");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse); // Trả về lỗi 409 cho email bị trùng
            }

            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Doctor added successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PutMapping("/edit-doctor/{id}")
    public ResponseEntity<?> editDoctor(@PathVariable Long id,
                                        @RequestParam("file") MultipartFile file,
                                        @ModelAttribute @Valid EditDoctorRequest editDoctorRequest, // sử dụng ModelAttribute để bind dữ liệu
                                        BindingResult bindingResult){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + errors);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        try {
            // Upload file lên Firebase Storage
            String fileUrl = firebaseStorageService.uploadFile(file);
            editDoctorRequest.setAvatarFilePath(fileUrl);

            apiResponse = editDoctorCommand.editDoctor(id,editDoctorRequest);
            // Check if the status code is 500 for duplicated code
            if (HttpStatus.INTERNAL_SERVER_ERROR.equals(apiResponse.getStatusCode())) {
                apiResponse.setMessage("Doctor's Email already exist in the system");
                return ResponseEntity.status(HttpStatus.OK).body(apiResponse); // Conflict for duplicated code
            }
            return new ResponseEntity<>(apiResponse, HttpStatus.OK); //  for success
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {

            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @DeleteMapping("/delete-doctor/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = deleteDoctorCommand.deleteByIdDoctor(id);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDoctorDetail(@PathVariable Long id)
    {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            apiResponse = getDoctorDetail.getDoctorDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/top-rating")
    public ResponseEntity<?> getTopRatingDoctor(){
        try{
            return new ResponseEntity<>(getTopTenRatingDoctorQuery.getTop10RatingDoctor(), HttpStatus.OK);
        }catch (Exception ex){
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @GetMapping("/get-doctors-for-booking")
    public ResponseEntity<List<DoctorBookingResponse>> getAllDoctorsForBooking(){
        var result = getAllDoctorsForBookingQuery.getAllDoctorsForBookingQuery();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
