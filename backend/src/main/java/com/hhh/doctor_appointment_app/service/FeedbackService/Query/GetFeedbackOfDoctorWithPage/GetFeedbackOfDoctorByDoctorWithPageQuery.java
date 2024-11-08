package com.hhh.doctor_appointment_app.service.FeedbackService.Query.GetFeedbackOfDoctorWithPage;


import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.FeedbackResponse.FeedbackResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.dto.response.ReplyFeedbackResponse.ReplyFeedbackResponse;
import com.hhh.doctor_appointment_app.entity.Feedback;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetFeedbackOfDoctorByDoctorWithPageQuery {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    public PageResponse<List<FeedbackResponse>> getFeedbackOfDoctorByDoctorEmail(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Feedback> feedbackPage = feedbackRepository.findAllByDoctor_Profile_Email(username,pageable);


        //Convert entities to responses
        List<FeedbackResponse> feedbackResponses = feedbackPage.getContent().stream()
                .map(feedback -> {
                    FeedbackResponse response = new FeedbackResponse();

                    PatientResponse patientResponse = patientMapper.toResponse(feedback.getPatient());
                    DoctorResponse doctorResponse = doctorMapper.toResponse(feedback.getDoctor());

                    response.setId(feedback.getId());
                    response.setComment(feedback.getComment());
                    response.setDateComment(feedback.getDateComment());
                    response.setRating(feedback.getRating());
                    response.setPatientResponse(patientResponse);
                    response.setDoctorResponse(doctorResponse);

                    // Chuyển đổi danh sách reply feedbacks
                    List<ReplyFeedbackResponse> replyFeedbackResponses = feedback.getReplyFeedbackList().stream()
                            .map(replyFeedback -> {
                                ReplyFeedbackResponse replyResponse = new ReplyFeedbackResponse();
                                replyResponse.setId(replyFeedback.getId());
                                replyResponse.setComment(replyFeedback.getComment());
                                replyResponse.setDateComment(replyFeedback.getDateComment());
                                replyResponse.setDoctorResponse(doctorResponse);
                                return replyResponse;
                            })
                            .collect(Collectors.toList());

                    response.setReplyFeedbackResponse(replyFeedbackResponses);

                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<FeedbackResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(feedbackResponses);
        double total = Math.ceil((double) feedbackPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);
        return pageResponse;
    }
}
