import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http: HttpClient) { }
  getListPatient():Observable<any>{
    return this.http.get(environment.apiEndpoint + '/api/patient/list-patient');
  }
}
