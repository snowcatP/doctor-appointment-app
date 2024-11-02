import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { host } from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class PatientService {

  private myInfo = `${host}/api/identity/myInfo`;

  private baseUrl = `${host}/api/patient`;

  constructor(private http: HttpClient) {}

  getPatientProfile(): Observable<any> {
    return this.http.get(`${this.myInfo}`);
  }

}
