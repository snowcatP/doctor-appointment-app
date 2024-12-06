package com.hhh.doctor_appointment_app.enums;

public enum AppointmentStatus {
    ACCEPT,   // Cuộc hẹn đã được chấp nhận và lên lịch
    IN_PROGRESS, // Cuộc hẹn đang diễn ra
    COMPLETED,   // Cuộc hẹn đã hoàn thành
    CANCELLED,   // Cuộc hẹn đã bị hủy
    PENDING,     // Cuộc hẹn đang chờ xử lý
    RESCHEDULED  // Cuộc hẹn được dời lại
}
