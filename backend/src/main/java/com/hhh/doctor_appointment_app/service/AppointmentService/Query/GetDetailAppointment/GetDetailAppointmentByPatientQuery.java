package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetDetailAppointment;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.response.ApiResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentDetailResponse;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.MedicalRecord;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDetailAppointmentByPatientQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;
    public ApiResponse<?> getAppointmentDetailByPatient(Long id){
        ApiResponse<AppointmentDetailResponse> apiResponse = new ApiResponse<>();
        try {
            Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment Not Found"));
            // Update
            AppointmentDetailResponse appointmentResponse = appointmentMapper.toAppointmentDetailResponse(appointment);
            appointmentResponse.setPatientId(appointment.getPatient().getId());
            appointmentResponse.setDoctor(doctorMapper.toResponse(appointment.getDoctor()));
            List<MedicalRecord> listMedicalRecord = medicalRecordRepository.findAllByPatient_Id(appointment.getPatient().getId());
            apiResponse.ok(appointmentResponse);
            apiResponse.setMessage("Get Appointment's Information Successfully");
        }catch(NotFoundException ex){
            apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            apiResponse.setMessage(ex.getMessage());
        }catch(Exception ex){
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
        }
        return apiResponse;
    }
}
