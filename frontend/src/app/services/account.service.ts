import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { host } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}
  onLogin(obj: any): Observable<any> {
    return this.http.post(host + '/api/auth/login', obj);
  }
  onSignupPatient(obj: any): Observable<any> {
    return this.http.post('api/auth/signup', obj);
  }
}
