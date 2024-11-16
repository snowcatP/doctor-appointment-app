import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { host } from '../../../environments/environment';
import { AddMedicalRecordRequest, EditMedicalRecordRequest } from '../models/medical-record.model';
import { ApiResponse } from '../models/patient.model';
@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {

  private medicalRecordUrl = `${host}/api/medical-record`;
  
  constructor(private http: HttpClient) {}

  getMedicalRecordsOfPatient(page: number, size: number): Observable<any> {
    const url = `${this.medicalRecordUrl}/list/patient?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getMedicalRecordsOfPatientByPatientId(id: number, page: number, size: number): Observable<any> {
    const url = `${this.medicalRecordUrl}/list/patient/${id}?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  addMedicalRecordForPatient(addMedicalRecordRequest: AddMedicalRecordRequest, file?: File): Observable<any> {
    const url = `${this.medicalRecordUrl}/add`;
    const formData: FormData = new FormData();

    // Check if the file exists before appending it to the formData
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob(),'');  // Append null if no file is selected
    }

    formData.append('description', addMedicalRecordRequest.description || '');
    formData.append('patientId', addMedicalRecordRequest.patientId.toString());
    formData.append('appointmentId', addMedicalRecordRequest.appointmentId.toString());

    return this.http.post(url, formData);
  }

  editMedicalRecordForPatient(editMedicalRecordRequest: EditMedicalRecordRequest, file?: File): Observable<any> {
    const url = `${this.medicalRecordUrl}/edit`;
    const formData: FormData = new FormData();

    // Check if the file exists before appending it to the formData
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob(),'');  // Append null if no file is selected
    }

    formData.append('description', editMedicalRecordRequest.description || '');
    formData.append('patientId', editMedicalRecordRequest.patientId.toString());
    formData.append('medicalRecordId', editMedicalRecordRequest.medicalRecordId.toString());
    return this.http.put(url, formData);
  }
}
