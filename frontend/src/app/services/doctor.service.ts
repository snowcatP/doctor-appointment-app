import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { host } from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  private baseUrl = `${host}/api/doctor`;

  constructor(private http: HttpClient) {}

  getDoctors(page: number, size: number): Observable<any> {
    const url = `${this.baseUrl}/list-doctor?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getDoctorDetail(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/detail/${id}`);
  }

  searchDoctors(searchDoctorRequest: any, page: number = 1, size: number = 10): Observable<any> {
    const url = `${this.baseUrl}/search?page=${page}&size=${size}`;
    return this.http.post(url, searchDoctorRequest);
  }
  
}
