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

  updatePatientProfile(data: UpdateProfileRequest): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(this.updateProfileUrl, data).pipe(
      tap(() => this.fetchMyInfo().subscribe()) // Fetch updated profile after successful update
    );
  }

  getAppointmentsOfPatient(page: number, size: number): Observable<any> {
    const url = `${this.appointmentUrl}/list/patient?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }
}
