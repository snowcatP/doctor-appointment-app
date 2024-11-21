package com.hhh.doctor_appointment_app.service.NotificationService.Implement;

import com.hhh.doctor_appointment_app.dto.response.NotificationResponse.BookingNotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingNotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBookingMessage(BookingNotificationResponse bookingNotificationResponse) {
        messagingTemplate.convertAndSend(
                "/app-ws/booking/notifications",
                bookingNotificationResponse);
    }
}
