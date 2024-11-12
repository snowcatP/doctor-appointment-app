import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {
  constructor(private http: HttpClient) { }
  getListSpecialty(): Observable<any>{
    return this.http.get<any>(environment.apiEndpoint + '/api/specialty/list-specialty')
  }

}
