package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentBookedResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.NotificationResponse.BookingNotificationResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mappings({
            @Mapping(source = "appointment.doctor.profile", target = "doctor")
    })
    AppointmentResponse toResponse(Appointment appointment);

    @Mapping(source = "appointment.doctor", target = "doctor")
    AppointmentBookedResponse toBookedResponse(Appointment appointment);

    List<AppointmentBookedResponse> toBookedResponses(List<Appointment> appointments);

    List<AppointmentResponse> toResponses(List<Appointment> appointments);

    @Mapping(source = "id", target = "appointmentId")
    BookingNotificationResponse toBookingNotificationResponse(Appointment appointment);
}
