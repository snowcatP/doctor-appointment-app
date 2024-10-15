import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { JwtHelperService } from '@auth0/angular-jwt';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private jwtHelper: JwtHelperService) {}
  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    // Check whether the token is expired and return
    // true or false
    if(this.jwtHelper.decodeToken(token) == null){
      return false;
    }

    return !this.jwtHelper.isTokenExpired(token);
  }
}
