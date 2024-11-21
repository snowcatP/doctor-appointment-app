import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError, BehaviorSubject } from 'rxjs';
import { host } from '../../../environments/environment';
import { DoctorBooking } from '../models/booking.model';
import { UpdateNurseProfileRequest } from '../models/nurse.model';
@Injectable({
  providedIn: 'root'
})
export class NurseService {
  private userDataSubject = new BehaviorSubject<null>(null);
  userData$ = this.userDataSubject.asObservable();

  private baseUrl = `${host}/api/nurse`;

  constructor(private http: HttpClient) { }


  getNurseProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/myInfo`);
  }

  setProfile(profile: any) {
    this.userDataSubject.next(profile); // Update the BehaviorSubject with new profile data
  }


  updateNurseProfile(updateNurseProfileRequest: UpdateNurseProfileRequest, file?: File): Observable<any> {
    const url = `${this.baseUrl}/update-profile`;
    const formData: FormData = new FormData();

    const formattedDateOfBirth = updateNurseProfileRequest.dateOfBirth
      ? new Date(updateNurseProfileRequest.dateOfBirth).toISOString().split('T')[0]
      : '';

    // Check if the file exists before appending it to the formData
    if (file) {
      formData.append('file', file, file.name);
    } else {
      formData.append('file', new Blob(),'');  // Append null if no file is selected
    }

    formData.append('firstName', updateNurseProfileRequest.firstName || '');
    formData.append('lastName', updateNurseProfileRequest.lastName || '');
    formData.append('gender', updateNurseProfileRequest.gender ? 'true' : 'false');
    formData.append('phone', updateNurseProfileRequest.phone || '');
    formData.append('dateOfBirth', formattedDateOfBirth);
    formData.append('address', updateNurseProfileRequest.address || '');

    return this.http.put(url, formData);
  }

  }
