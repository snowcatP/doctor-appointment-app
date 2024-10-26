package com.hhh.doctor_appointment_app.service.DoctorService.Query.CalculateAverageRatingDoctor;

import com.hhh.doctor_appointment_app.entity.Feedback;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateAverageRatingDoctorQuery {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public double calculateAverageRating(Long doctorId) {
        // Lấy danh sách tất cả các feedback theo doctorId
        List<Feedback> feedbacks = feedbackRepository.findAllByDoctor_Id(doctorId);

        // Kiểm tra xem có feedback nào không
        if (feedbacks.isEmpty()) {
            return 0.0;  // Nếu không có feedback, trả về 0
        }

        // Tính tổng rating
        double totalRating = feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .sum();

        // Tính trung bình
        return totalRating / feedbacks.size();
    }
}
