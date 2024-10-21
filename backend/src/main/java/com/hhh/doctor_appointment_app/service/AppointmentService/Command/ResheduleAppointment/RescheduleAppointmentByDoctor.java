package com.hhh.doctor_appointment_app.service.AppointmentService.Command.ResheduleAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.RescheduleWithDateRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.state.AcceptState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RescheduleAppointmentByDoctor {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @PreAuthorize("hasRole('DOCTOR')")
    public ApiResponse<Object> rescheduleAppointmentByDoctor(Long id, RescheduleWithDateRequest rescheduleWithDateRequest) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try {
            // Tìm kiếm Appointment theo id
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Appointment Not Found"));

            // Kiểm tra trạng thái hiện tại để đảm bảo chỉ có thể dời lịch từ trạng thái SCHEDULED
            if (!(appointment.getAppointmentState() instanceof AcceptState)) {
                throw new IllegalStateException("Appointment can only be rescheduled from Scheduled status.");
            }

            // Thực hiện việc chuyển trạng thái sang RESCHEDULED
            ((AcceptState) appointment.getAppointmentState()).reschedule(appointment, rescheduleWithDateRequest.getNewDateBooking());

            // Lưu lại appointment với trạng thái RESCHEDULED
            appointmentRepository.saveAndFlush(appointment);

            // Tạo phản hồi từ Appointment sau khi thay đổi
            AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointment);
            apiResponse.setMessage("Appointment Rescheduled Successfully!");
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
                    .message("Appointment Reschedule Failed!")
                    .build();
        }
    }
}
