import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { JwtHelperService } from '@auth0/angular-jwt';
import {
  LoginRequest,
  LoginSucessResponse,
  RegisterRequest,
  User,
} from '../models/authentication.model';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { host } from '../../../environments/environment';
import { Store } from '@ngrx/store';
import * as AuthActions from '../states/auth/auth.actions';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private jwtHelper: JwtHelperService,
    private http: HttpClient,
    private store: Store
  ) {}

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (this.jwtHelper.decodeToken(token) == null) {
      return false;
    }
    return !this.jwtHelper.isTokenExpired(token);
  }

  refreshToken(): Observable<LoginSucessResponse> {
    return this.http
      .post<LoginSucessResponse>(host + '/api/auth/refreshToken', {
        token: localStorage.getItem('token'),
      })
      .pipe(
        tap((res) => {
          localStorage.setItem('token', res.token);
          this.store.dispatch(
            AuthActions.refreshToken({ refreshTokenResponse: res })
          );
        }),
        catchError((error) => {
          console.error('Error refreshing access token:', error);
          return throwError(error);
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post<any>(host + '/api/auth/logout', {
      token: localStorage.getItem('token'),
    });
  }

  getUserData(): Observable<User> {
    return this.http.get<User>(host + '/api/identity/myInfo');
  }

  onLogin(data: LoginRequest): Observable<LoginSucessResponse> {
    return this.http.post<LoginSucessResponse>(host + '/api/auth/login', data);
  }
  onSignupPatient(data: RegisterRequest): Observable<RegisterRequest> {
    return this.http.post<RegisterRequest>(host + '/api/auth/signup', data);
  }
}
