package com.hhh.doctor_appointment_app.service;

import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyMapper;
import com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest.AddSpecialtyRequest;
import com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest.EditSpecialtyRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    public PageResponse<List<SpecialtyResponse>> getSpecialtiesWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Specialty> specialtyPage = specialtyRepository.getSpecialtiesWithPage(pageable);

        //Convert entities to responses
        List<SpecialtyResponse> specialtyResponses = specialtyPage.getContent().stream()
                .map(specialty -> {
                    SpecialtyResponse response = new SpecialtyResponse();
                    response.setId(specialty.getId());
                    response.setSpecialtyName(specialty.getSpecialtyName());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<SpecialtyResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(specialtyResponses);
        double total = Math.ceil((double) specialtyPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }

    public ApiResponse<Object> addSpecialty(AddSpecialtyRequest addSpecialtyRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Specialty newSpecialty = new Specialty();
            newSpecialty.setSpecialtyName(addSpecialtyRequest.getSpecialtyName());


            boolean isDuplicate = specialtyRepository.existsBySpecialtyName(newSpecialty.getSpecialtyName());
            if(isDuplicate){
                apiResponse.duplicatedCode();
                return apiResponse;
            }

            specialtyRepository.saveAndFlush(newSpecialty);
            SpecialtyResponse specialtyResponse = specialtyMapper.toResponse(newSpecialty);
            apiResponse.ok(specialtyResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException("An unexpected error occurred");
        }
    }

    public ApiResponse<Object> editSpecialty(Long id, EditSpecialtyRequest editSpecialtyRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            Specialty existingSpecialty = specialtyRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Specialty Not Found"));

            // Kiểm tra xem name trong edit request có khác với name của khoa hiện tại hay không
            if (!editSpecialtyRequest.getSpecialtyName().equals(existingSpecialty.getSpecialtyName())) {
                // Nếu khác, kiểm tra xem name này có tồn tại trong hệ thống hay không (ngoại trừ khoa hiện tại)
                boolean isDuplicate = specialtyRepository.existsBySpecialtyName(editSpecialtyRequest.getSpecialtyName());
                if (isDuplicate) {
                    // Nếu name đã tồn tại, trả về lỗi duplicate
                    apiResponse.duplicatedCode();
                    return apiResponse;
                }
            }

            existingSpecialty.setSpecialtyName(editSpecialtyRequest.getSpecialtyName());

            specialtyRepository.saveAndFlush(existingSpecialty);

            SpecialtyResponse specialtyResponse = specialtyMapper.toResponse(existingSpecialty);
            apiResponse.ok(specialtyResponse);
        } catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch (ApplicationException ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage("Failed to update");
        }
        return apiResponse;
    }

    public ApiResponse<?> deleteByIdSpecialty(Long id){
        ApiResponse<SpecialtyResponse> apiResponse = new ApiResponse<>();

        try {
            Specialty specialty = specialtyRepository.findById(id).orElseThrow(() -> new NotFoundException("Specialty Not Found"));
            specialtyRepository.deleteById(specialty.getId());
            apiResponse.ok();
            apiResponse.setMessage("Specialty successfully deleted");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }

    public ApiResponse<?> getSpecialtyDetail(Long id){
        ApiResponse<SpecialtyResponse> apiResponse = new ApiResponse<>();
        try {
            Specialty specialty = specialtyRepository.findById(id).orElseThrow(() -> new NotFoundException("Specialty Not Found"));
            // Update
            SpecialtyResponse specialtyResponse = specialtyMapper.toResponse(specialty);
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
