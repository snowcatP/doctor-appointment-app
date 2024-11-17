package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetDoctorWithPage;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Feedback;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.CalculateAverageRatingDoctor.CalculateAverageRatingDoctorQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetDoctorWithPageQuery {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CalculateAverageRatingDoctorQuery calculateAverageRatingDoctorQuery;

    public List<DoctorResponse> getDoctorsWithPage(int page, int size) {

        List<Doctor> doctors = doctorRepository.findAll();
        //Convert entities to responses
        // Convert entities to responses
        List<DoctorResponse> doctorResponses = doctors.stream()
                .map(doctor -> {
                    double ratingOfDoctor = calculateAverageRatingDoctorQuery.calculateAverageRating(doctor.getId());
                    List<Feedback> feedbacks = feedbackRepository.findAllByDoctor_Id(doctor.getId());

                    DoctorResponse response = new DoctorResponse();
                    response.setId(doctor.getId());
                    response.setFirstName(doctor.getProfile().getFirstName());
                    response.setLastName(doctor.getProfile().getLastName());
                    response.setGender(doctor.getProfile().isGender());
                    response.setPhone(doctor.getProfile().getPhone());
                    response.setEmail(doctor.getProfile().getEmail());
                    response.setDateOfBirth(doctor.getProfile().getDateOfBirth());
                    response.setAddress(doctor.getProfile().getAddress());
                    response.setSpecialty(doctor.getSpecialty());
                    response.setAvatarFilePath(doctor.getProfile().getAvatarFilePath());
                    response.setAverageRating(ratingOfDoctor);
                    response.setNumberOfFeedbacks(feedbacks.size());

                    return response;
                })
                .collect(Collectors.toList());

        /// Return the list of doctor responses
        return doctorResponses;
    }
}
