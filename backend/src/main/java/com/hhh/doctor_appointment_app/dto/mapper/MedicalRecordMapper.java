package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse.MedicalRecordResponse;
import com.hhh.doctor_appointment_app.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    @Mappings({
            @Mapping(source = "medicalRecord.doctorModified", target = "doctorResponse"),
            @Mapping(source = "medicalRecord.dateCreated", target = "dateCreated"),
            @Mapping(source = "medicalRecord.lastModified", target = "lastModified"),
            @Mapping(source = "medicalRecord.patient.profile.email", target = "emailPatient")
    })
    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);
}
