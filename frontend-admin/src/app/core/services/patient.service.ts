import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Patient } from '../models/patient';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  constructor(private http: HttpClient) {}
  getListPatient(): Observable<any> {
    return this.http.get(environment.apiEndpoint + '/api/patient/list-patient');
  }
  getPatientDetail(id: number): Observable<any> {
    return this.http.get(environment.apiEndpoint + `/api/patient/detail/${id}`);
  }
  editPatient(patient: Patient, file: File, id: string): Observable<any> {
    const formData = new FormData();
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', '');
    }
    formData.append('firstName', patient.firstName);
    formData.append('lastName', patient.lastName);
    formData.append('password', patient.password);
    formData.append('gender', patient.gender.toString());
    formData.append('address', patient.address);
    formData.append('dateOfBirth', new Date(patient.dateOfBirth).toISOString());
    formData.append('email', patient.email);
    formData.append('phone', patient.phone);
    return this.http.put<any>(
      environment.apiEndpoint + `/api/patient/edit-patient/${id}`,
      formData
    );
  }
}
