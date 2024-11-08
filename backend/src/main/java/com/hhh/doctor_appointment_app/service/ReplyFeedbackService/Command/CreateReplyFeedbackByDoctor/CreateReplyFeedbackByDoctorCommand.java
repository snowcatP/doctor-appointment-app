package com.hhh.doctor_appointment_app.service.ReplyFeedbackService.Command.CreateReplyFeedbackByDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.FeedbackMapper;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.mapper.ReplyFeedbackMapper;
import com.hhh.doctor_appointment_app.dto.request.ReplyFeedbackRequest.ReplyFeedbackByDoctorRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.FeedbackResponse.FeedbackResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.dto.response.ReplyFeedbackResponse.ReplyFeedbackResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Feedback;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.ReplyFeedback;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.repository.ReplyFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateReplyFeedbackByDoctorCommand {
    @Autowired
    private ReplyFeedbackRepository replyFeedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private ReplyFeedbackMapper replyFeedbackMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ApiResponse<Object> createReplyFeedbackByDoctor(ReplyFeedbackByDoctorRequest replyFeedbackByDoctorRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Feedback feedback = feedbackRepository.findById(replyFeedbackByDoctorRequest.getReplyCommentID())
                    .orElseThrow(() -> new NotFoundException("Not found Feedback"));

            Patient patient = patientRepository.findById(replyFeedbackByDoctorRequest.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Not found patient"));

            Doctor doctor = doctorRepository.findById(replyFeedbackByDoctorRequest.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Not found doctor"));

            ReplyFeedback replyFeedback = new ReplyFeedback();
            replyFeedback.setComment(replyFeedbackByDoctorRequest.getComment());
            replyFeedback.setDateComment(new Date());
            replyFeedback.setFeedback(feedback);
            replyFeedback.setPatient(patient);
            replyFeedback.setDoctor(doctor);


            replyFeedbackRepository.saveAndFlush(replyFeedback);

            PatientResponse patientResponse = patientMapper.toResponse(replyFeedback.getPatient());
            DoctorResponse doctorResponse = doctorMapper.toResponse(replyFeedback.getDoctor());
            FeedbackResponse feedbackResponse = feedbackMapper.toResponse(replyFeedback.getFeedback());

            ReplyFeedbackResponse replyFeedbackResponse = replyFeedbackMapper.toResponse(replyFeedback);
            replyFeedbackResponse.setDoctorResponse(doctorResponse);
            apiResponse.ok(replyFeedbackResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException(String.valueOf(ex));
        }
    }
}
