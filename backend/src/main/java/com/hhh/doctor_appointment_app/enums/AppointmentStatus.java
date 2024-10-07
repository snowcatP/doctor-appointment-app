package com.hhh.doctor_appointment_app.enums;

public enum AppointmentStatus {
    SCHEDULED,   // Cuộc hẹn đã được lên lịch
    COMPLETED,   // Cuộc hẹn đã hoàn thành
    CANCELLED,   // Cuộc hẹn đã bị hủy
    PENDING,     // Cuộc hẹn đang chờ xử lý
    RESCHEDULED  // Cuộc hẹn được dời lại
}
