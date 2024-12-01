import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { AppointmentResponse } from '../models/appointment';

@Injectable({
  providedIn: 'root'
})
export class AppoinmentService {

  constructor(private http: HttpClient) { }
  getListAppointment(): Observable<any>{
    return this.http.get(environment.apiEndpoint + '/api/appointment/list');
  }
  getAppointmentDetail(id: number): Observable<any>{
    return this.http.get<AppointmentResponse>(environment.apiEndpoint + `/api/appointment/patient/detail/${id}`)
  }
}
