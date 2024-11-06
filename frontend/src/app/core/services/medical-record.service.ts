import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { host } from '../../../environments/environment';
import { UpdateProfileRequest } from '../models/patient.model';
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
}
