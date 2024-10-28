import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { host } from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  private baseUrl = `${host}/api/feedback`;

  constructor(private http: HttpClient) {}

  getFeedbacksOfDoctorByDoctorID(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/list/doctor/${id}`);
  }

}
