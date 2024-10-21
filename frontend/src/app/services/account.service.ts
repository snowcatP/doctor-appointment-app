import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  constructor(private http: HttpClient) {}
  onLogin(obj: any): Observable<any> {
    return this.http.post('api/auth/login', obj);
  }
  onSignupPatient(obj: any): Observable<any> {
    return this.http.post('api/auth/signup', obj);
  }
}
