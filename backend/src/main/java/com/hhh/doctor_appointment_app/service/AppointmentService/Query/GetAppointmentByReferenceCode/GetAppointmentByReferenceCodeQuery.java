package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentByReferenceCode;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.ReferenceCodeRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetAppointmentByReferenceCodeQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;
    public ApiResponse<?> getAppointmentByReferenceCode(ReferenceCodeRequest referenceCodeRequest){
        ApiResponse<AppointmentResponse> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findAppointmentByReferenceCode(referenceCodeRequest.getReferenceCode())
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));
            // Update
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.ok(appointmentResponse);
            apiResponse.setMessage("Get Appointment's Information Successfully");
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

