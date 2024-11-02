import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, of, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleProviderService {

  constructor(
    private authService: AuthService,
    private jwtHelperService: JwtHelperService
  ) {}
  getRole(): Observable<string> {
    return of(null).pipe(
      map(() => localStorage.getItem('token')),
      map(token => this.authService.isAuthenticated() 
        ? this.jwtHelperService.decodeToken(token)['scope'] 
        : 'GUEST'
      )
    );
  }
}
