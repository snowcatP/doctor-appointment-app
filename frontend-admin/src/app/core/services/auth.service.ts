import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private jwtHelper: JwtHelperService) {}
  public isAuthenticated(): boolean {
     // Check whether the token is expired and return
     // true or false
    const token = localStorage.getItem('token');
    if(token && token != 'null'){
      const decodeToken = this.jwtHelper.decodeToken(String(token));
      if( decodeToken['scope'] != "ADMIN" )
        return false;
    }
    else{
      return false;
    }
    return !this.jwtHelper.isTokenExpired(token);
  }
}
