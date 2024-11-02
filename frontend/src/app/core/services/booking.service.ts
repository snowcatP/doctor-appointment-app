import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorBooking } from '../models/booking.model';
import { host } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor(private http: HttpClient) { }

  getDoctors(): Observable<DoctorBooking[]> {
    return this.http.get<DoctorBooking[]>(host + '/api/doctor/get-doctors-for-booking');
  }
}
