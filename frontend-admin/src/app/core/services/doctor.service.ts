import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Doctor } from '../models/doctor';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private http: HttpClient) { }
  getListDoctor(): Observable<any>{
    return this.http.get('http://localhost:8080/api/doctor/list-doctor');
  }
}
