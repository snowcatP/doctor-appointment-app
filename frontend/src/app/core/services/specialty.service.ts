import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { host } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {

  private baseUrl = `${host}/api/specialty`;

  constructor(private http: HttpClient) {}

  getListSpecialty(): Observable<any> {
    return this.http.get(`${this.baseUrl}/all`);
  }
}
