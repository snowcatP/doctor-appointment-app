import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {

  constructor(private http: HttpClient) { }
  getListMedicalRecordByPatient(id: number): Observable<any>{
    return this.http.get(environment.apiEndpoint + `/api/medical-record/list/patient/${id}`)
  }
 
}
