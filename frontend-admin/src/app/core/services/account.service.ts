import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/authentication';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  constructor(private http: HttpClient) { }
  onLogin(data: LoginRequest){
    return this.http.post<LoginRequest>(environment.apiEndpoint + '/api/auth/login', data)
  }
  onLogout(data: string){
    return this.http.post<LoginRequest>(environment.apiEndpoint + '/api/auth/logout', JSON.stringify(data))
  }
}
