import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {
  AppointmentsBooked,
  BookingDataGuest,
  BookingDataPatient,
} from '../models/booking.model';
import { host } from '../../../environments/environment';
import { Appointment, RescheduleAppointment } from '../models/appointment.model';
import { ApiResponse } from '../models/doctor.model';
import { AppointmentResponse } from '../models/appointment.model';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private appointmentDataGuest = new BehaviorSubject(new BookingDataGuest());
  getAppointmentBookedGuest = this.appointmentDataGuest.asObservable();

  private appointmentDataPatient = new BehaviorSubject(
    new BookingDataPatient()
  );
  getAppointmentBookedPatient = this.appointmentDataPatient.asObservable();

  constructor(private http: HttpClient) {}

  getAllAppointmentsByDoctorEmail(): Observable<AppointmentResponse[]> {
    return this.http.get<AppointmentResponse[]>(
      `${host}/api/appointment/get-all-appointments-by-doctor`
    );
  }

  getAppointmentsForBooking(
    doctorId: number
  ): Observable<AppointmentsBooked[]> {
    return this.http.get<AppointmentsBooked[]>(
      `${host}/api/appointment/get-appointments-for-booking/${doctorId}`
    );
  }

  createAppointmentByGuest(bookingData: BookingDataGuest): Observable<any> {
    return this.http.post<any>(
      `${host}/api/appointment/guest/create-appointment`,
      bookingData
    );
  }

  createAppointmentByPatient(bookingData: BookingDataPatient): Observable<any> {
    return this.http.post<any>(
      `${host}/api/appointment/patient/create-appointment`,
      bookingData
    );
  }

  setAppointmentBookedGuest(appointment: BookingDataGuest) {
    this.appointmentDataGuest.next(appointment);
  }
  setAppointmentBookedPatient(appointment: BookingDataPatient) {
    this.appointmentDataPatient.next(appointment);
  }

  getAppointmentsOfPatientByPatientId(id: number, page: number, size: number): Observable<any> {
    const url = `${host}/api/appointment/list/patient/${id}?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getListAppointmentsOfDoctor(page: number, size: number): Observable<any> {
    const url = `${host}/api/appointment/list/doctor?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  changeStatusAppointmentByDoctor(id: number): Observable<any> {
    const url = `${host}/api/appointment/change-status/${id}`;
    return this.http.put<ApiResponse>(url,id);
  }

  cancelAppointmentByDoctor(id: number): Observable<any> {
    const url = `${host}/api/appointment/cancel/${id}`;
    return this.http.put<ApiResponse>(url,id);
  }

  getAppointmentsForRescheduling(): Observable<AppointmentsBooked[]> {
    return this.http.get<AppointmentsBooked[]>(
      `${host}/api/appointment/get-appointments-for-rescheduling`
    );
  }

  rescheduleAppointmentByDoctor(id: number, rescheduleAppointment: RescheduleAppointment): Observable<any>{
    const url = `${host}/api/appointment/reschedule/${id}`;
    return this.http.put<ApiResponse>(url,rescheduleAppointment);
  }
}
