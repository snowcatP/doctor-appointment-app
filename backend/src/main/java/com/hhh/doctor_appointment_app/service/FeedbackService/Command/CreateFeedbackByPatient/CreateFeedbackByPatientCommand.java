package com.hhh.doctor_appointment_app.service.FeedbackService.Command.CreateFeedbackByPatient;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.FeedbackMapper;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.request.FeedbackRequest.CreateFeedbackByPatientRequest;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.FeedbackResponse.FeedbackResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.entity.*;
import com.hhh.doctor_appointment_app.exception.ApplicationException;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.FeedbackRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateFeedbackByPatientCommand
{
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public ApiResponse<Object> createFeedbackByPatient(CreateFeedbackByPatientRequest createFeedbackByPatientRequest){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        try{
            Patient patient = patientRepository.findById(createFeedbackByPatientRequest.getPatientId())
                    .orElseThrow(() -> new NotFoundException("Not found patient"));

            Doctor doctor = doctorRepository.findById(createFeedbackByPatientRequest.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Not found doctor"));

            Feedback newFeedback = new Feedback();
            newFeedback.setComment(createFeedbackByPatientRequest.getComment());
            newFeedback.setDateComment(new Date());
            newFeedback.setRating(createFeedbackByPatientRequest.getRating());
            newFeedback.setPatient(patient);
            newFeedback.setDoctor(doctor);


            feedbackRepository.saveAndFlush(newFeedback);

            PatientResponse patientResponse = patientMapper.toResponse(newFeedback.getPatient());
            DoctorResponse doctorResponse = doctorMapper.toResponse(newFeedback.getDoctor());
            FeedbackResponse feedbackResponse = feedbackMapper.toResponse(newFeedback);
            feedbackResponse.setPatientResponse(patientResponse);
            feedbackResponse.setDoctorResponse(doctorResponse);
            apiResponse.ok(feedbackResponse);
            return apiResponse;
        }catch (Exception ex){
            throw new ApplicationException(String.valueOf(ex));
        }
    }
}
