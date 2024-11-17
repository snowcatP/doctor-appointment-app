import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { host } from '../../../environments/environment';
import { UpdateProfileRequest } from '../models/patient.model';
import { ApiResponse } from '../models/patient.model';
import { tap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private myInfoUrl = `${host}/api/identity/myInfo`;
  private updateProfileUrl = `${host}/api/identity/user/update-profile`;
  private baseUrl = `${host}/api/patient`;
  private appointmentUrl = `${host}/api/appointment`;
  private patientProfileSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  
  constructor(private http: HttpClient) {}

  setPatientProfile(profile: any) {
    this.patientProfileSubject.next(profile);
  }

  getPatientProfile(): Observable<any> {
    return this.patientProfileSubject.asObservable();
  }
  
  fetchMyInfo(): Observable<any> {
    return this.http.get<any>(this.myInfoUrl).pipe(
      tap(profile => this.patientProfileSubject.next(profile))
    );
  }

  updatePatientProfile(updateProfileRequest: UpdateProfileRequest, file?: File): Observable<ApiResponse> {
    const formData: FormData = new FormData();

    const formattedDateOfBirth = updateProfileRequest.dateOfBirth
      ? new Date(updateProfileRequest.dateOfBirth).toISOString().split('T')[0]
      : '';

    // Check if the file exists before appending it to the formData
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob(),'');  // Append null if no file is selected
    }

    formData.append('email', updateProfileRequest.email || '');
    formData.append('firstName', updateProfileRequest.firstName || '');
    formData.append('lastName', updateProfileRequest.lastName || '');
    formData.append('gender', updateProfileRequest.gender ? 'true' : 'false');
    formData.append('phone', updateProfileRequest.phone || '');
    formData.append('dateOfBirth', formattedDateOfBirth);
    formData.append('address', updateProfileRequest.address || '');

    return this.http.put<ApiResponse>(this.updateProfileUrl, formData).pipe(
      tap(() => this.fetchMyInfo().subscribe()) // Fetch updated profile after successful update
    );
  }

  getAppointmentsOfPatient(page: number, size: number): Observable<any> {
    const url = `${this.appointmentUrl}/list/patient?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getPatientDetail(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/detail/${id}`);
  }
}
