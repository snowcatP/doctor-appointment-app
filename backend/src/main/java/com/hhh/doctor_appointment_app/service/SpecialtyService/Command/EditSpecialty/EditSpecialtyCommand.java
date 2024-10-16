package com.hhh.doctor_appointment_app.service.SpecialtyService.Command.EditSpecialty;

import com.hhh.doctor_appointment_app.dto.mapper.SpecialtyMapper;
import com.hhh.doctor_appointment_app.dto.request.SpecialtyRequest.EditSpecialtyRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.SpecialtyResponse.SpecialtyResponse;
import com.hhh.doctor_appointment_app.entity.Specialty;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EditSpecialtyCommand {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;
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
}
