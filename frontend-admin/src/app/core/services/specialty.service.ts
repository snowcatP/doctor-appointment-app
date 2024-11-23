import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Specialty } from '../models/speciality';

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {
  constructor(private http: HttpClient) { }
  getListSpecialty(): Observable<any>{
    return this.http.get<any>(environment.apiEndpoint + '/api/specialty/list-specialty')
  }
  getSpecialtyDetail(id: number): Observable<any>{
    return this.http.get(environment.apiEndpoint +`/api/specialty/detail/${id}`);
  }
  addNewSpecialty(specialtyReqeust: Specialty): Observable<any> {
    return this.http.post<any>(environment.apiEndpoint + '/api/specialty/add-specialty', specialtyReqeust);
  }
  deleteSpecialty(id:number): Observable<any>{
    return this.http.delete<any>(environment.apiEndpoint + `/api/specialty/delete-specialty/${id}`);
  }

}
