import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Doctor } from '../models/doctor';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private http: HttpClient) { }
  getListDoctor(): Observable<any>{
    return this.http.get(environment.apiEndpoint +'/api/doctor/list-doctor');
  }
  addNewDoctor(doctor: Doctor, file: File): Observable<any> {
    const formData = new FormData();
    if(file){
      formData.append('file', file, file.name);
    }else{
      formData.append('file', new Blob(), '')
    }
    formData.append('firstName', doctor.firstName);
    formData.append('lastName', doctor.lastName);
    formData.append('password', doctor.password);
    formData.append('gender', (doctor.gender).toString());
    formData.append('address', doctor.address);
    formData.append('dateOfBirth', new Date(doctor.dateOfBirth).toISOString());
    formData.append('email', doctor.email);
    formData.append('phone', doctor.phone);
    formData.append('specialtyID', doctor.specialtyId);
    return this.http.post<any>(environment.apiEndpoint + '/api/doctor/add-doctor', formData);
  }
}
