package com.hhh.doctor_appointment_app.service.AppointmentService.Command.CancelAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.state.CancelledState;
import com.hhh.doctor_appointment_app.state.PendingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CancelAppointmentCommand {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> cancelAppointmentByDoctor(Long id) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            // Tìm kiếm Appointment theo id
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            // Kiểm tra trạng thái hiện tại để đảm bảo chỉ có thể dời lịch từ trạng thái SCHEDULED
            if (!(appointment.getAppointmentState() instanceof PendingState)) {
                throw new IllegalStateException("Appointment can only be cancelled from Pending status.");
            }

            appointment.setAppointmentState(new CancelledState());

            // Lưu lại appointment với trạng thái CANCELLED
            appointmentRepository.saveAndFlush(appointment);

            // Tạo phản hồi từ Appointment sau khi thay đổi
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.setMessage("Appointment Cancelled Successfully!");
            apiResponse.ok(appointmentResponse);
            return apiResponse;

        } catch (NotFoundException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .build();
        } catch (IllegalStateException ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .build();
        } catch (Exception ex) {
            return ApiResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Appointment Cancelled Failed!")
                    .build();
        }
    }
}
