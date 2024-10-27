import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, RegisterRequest } from '../models/authentication.model';
import { apiEndpoint } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}
  onLogin(data: LoginRequest): Observable<LoginRequest> {
    return this.http.post<LoginRequest>(apiEndpoint + '/api/auth/login', data);
  }
  onSignupPatient(data: RegisterRequest): Observable<RegisterRequest> {
    return this.http.post<RegisterRequest>(apiEndpoint + '/api/auth/signup', data);
  }
}
