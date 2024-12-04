import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError, BehaviorSubject } from 'rxjs';
import { host } from '../../../environments/environment';
import { DoctorBooking } from '../models/booking.model';
import {
  DoctorChatResponse,
  UpdateDoctorProfileRequest,
} from '../models/doctor.model';
@Injectable({
  providedIn: 'root',
})
export class DoctorService {
  private userDataSubject = new BehaviorSubject<null>(null);
  userData$ = this.userDataSubject.asObservable();

  private baseUrl = `${host}/api/doctor`;

  constructor(private http: HttpClient) {}

  getDoctors(page: number, size: number): Observable<any> {
    const url = `${this.baseUrl}/list-doctor?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getDoctorDetail(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/detail/${id}`);
  }

  searchDoctors(
    searchDoctorRequest: any,
    page: number = 1,
    size: number = 10
  ): Observable<any> {
    const url = `${this.baseUrl}/search?page=${page}&size=${size}`;
    return this.http.post(url, searchDoctorRequest);
  }

  getTopRatingDoctors(): Observable<any> {
    const url = `${this.baseUrl}/top-rating`;
    return this.http.get<any>(url);
  }

  getDoctorsForBooking(): Observable<DoctorBooking[]> {
    return this.http.get<DoctorBooking[]>(
      `${host}/api/doctor/get-doctors-for-booking`
    );
  }

  getDoctorProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/myInfo`);
  }

  updateDoctorProfile(
    updateDoctorProfileRequest: UpdateDoctorProfileRequest,
    file?: File
  ): Observable<any> {
    const url = `${this.baseUrl}/update-profile`;
    const formData: FormData = new FormData();

    const formattedDateOfBirth = updateDoctorProfileRequest.dateOfBirth
      ? new Date(updateDoctorProfileRequest.dateOfBirth)
          .toISOString()
          .split('T')[0]
      : '';

    // Check if the file exists before appending it to the formData
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob(), ''); // Append null if no file is selected
    }

    formData.append('firstName', updateDoctorProfileRequest.firstName || '');
    formData.append('lastName', updateDoctorProfileRequest.lastName || '');
    formData.append(
      'gender',
      updateDoctorProfileRequest.gender ? 'true' : 'false'
    );
    formData.append('phone', updateDoctorProfileRequest.phone || '');
    formData.append('dateOfBirth', formattedDateOfBirth);
    formData.append('address', updateDoctorProfileRequest.address || '');

    return this.http.put(url, formData);
  }

  setProfile(profile: any) {
    this.userDataSubject.next(profile); // Update the BehaviorSubject with new profile data
  }

  getAllPatientsOfDoctor(
    page: number,
    size: number,
    patientName: string = ''
  ): Observable<any> {
    const url = `${this.baseUrl}/all-patient?page=${page}&size=${size}&patientName=${patientName}`;
    return this.http.get<any>(url);
  }

  getAllDoctorsHaveBookedByPatient(): Observable<DoctorChatResponse[]> {
    return this.http.get<DoctorChatResponse[]>(
      `${this.baseUrl}/get-all-doctors-booked-of-patient`
    );
  }

  //Share doctor's information when click Book Appointment button
  private doctorIdSource = new BehaviorSubject<number>(null);
  currentDoctorId = this.doctorIdSource.asObservable();

  setDoctorId(doctorId: number) {
    this.doctorIdSource.next(doctorId);
  }
}
