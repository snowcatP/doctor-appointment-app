package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetAllDoctorsForBooking;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorBookingResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Feedback;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import com.hhh.doctor_appointment_app.service.DoctorService.Query.CalculateAverageRatingDoctor.CalculateAverageRatingDoctorQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllDoctorsForBookingQuery {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private CalculateAverageRatingDoctorQuery calculateAverageRatingDoctorQuery;

    @Autowired
    private FeedbackRepository feedbackRepository;


    public List<DoctorBookingResponse> getAllDoctorsForBookingQuery() {
        List<Doctor> doctorList = doctorRepository.findAll();
        List<DoctorBookingResponse> doctorBookingResponseList = doctorList.stream().
                map(doctor -> {
                    double ratingOfDoctor = calculateAverageRatingDoctorQuery.calculateAverageRating(doctor.getId());
                    List<Feedback> feedbacks = feedbackRepository.findAllByDoctor_Id(doctor.getId());

                    DoctorBookingResponse response = new DoctorBookingResponse();
                    response.setId(doctor.getId());
                    response.setFirstName(doctor.getProfile().getFirstName());
                    response.setLastName(doctor.getProfile().getLastName());
                    response.setFullName(doctor.getProfile().getFullName());
                    response.setAddress(doctor.getProfile().getAddress());
                    response.setSchedule(doctor.getSchedule());
                    response.setAvatarFilePath(doctor.getProfile().getAvatarFilePath());
                    response.setSpecialty(doctor.getSpecialty());
                    response.setAverageRating(ratingOfDoctor);
                    response.setNumberOfFeedbacks(feedbacks.size());
                    return response;
                        }

                ).collect(Collectors.toList());
        return doctorBookingResponseList;
    }

}
