import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { host } from '../../../environments/environment';
import { FeedbackRequest, ApiResponse, ReplyFeedbackRequest } from '../models/feedback.model';
@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  private baseUrl = `${host}/api/feedback`;

  constructor(private http: HttpClient) {}

  getFeedbacksOfDoctorByDoctorID(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/list/doctor/${id}`);
  }

  createFeedbackForDoctorByPatient(data: FeedbackRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/patient/create`, data);
  }

  getFeedbackOfDoctorByDoctorEmail(page: number, size: number): Observable<any> {
    const url = `${this.baseUrl}/doctor/all?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  replyFeedbackForPatientByDoctor(data: ReplyFeedbackRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/doctor/reply`, data);
  }
}
