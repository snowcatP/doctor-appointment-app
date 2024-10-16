package com.hhh.doctor_appointment_app.service.MedicalRecordService.Query.GetMedicalRecordWithPage;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.MedicalRecord;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.MedicalRecordRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class GetMedicalRecordWithPageQuery {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    public PageResponse<List<MedicalRecordResponse>> getMedicalRecordsWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<MedicalRecord> medicalRecordPage = medicalRecordRepository.getMedicalRecordsWithPage(pageable);

        //Convert entities to responses
        List<MedicalRecordResponse> medicalRecordResponses = medicalRecordPage.getContent().stream()
                .map(medicalRecord -> {
                    MedicalRecordResponse response = new MedicalRecordResponse();
                    response.setId(medicalRecord.getId());
                    response.setDescription(medicalRecord.getDescription());
                    response.setFilePath(medicalRecord.getFilePath());
                    response.setDateCreated(medicalRecord.getDateCreated());
                    response.setLastModified(medicalRecord.getLastModified());
                    response.setDoctorResponse(doctorMapper.toResponse(medicalRecord.getDoctorModified()));
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<MedicalRecordResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(medicalRecordResponses);
        double total = Math.ceil((double) medicalRecordPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }
}
