import { Injectable } from '@angular/core';
import { NbRoleProvider } from '@nebular/security';
import { map, Observable, of } from 'rxjs';
import { AuthService } from './auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
@Injectable({
  providedIn: 'root',
})
export class RoleProviderService implements NbRoleProvider {
  constructor(
    private authService: AuthService,
    private jwtHelperService: JwtHelperService
  ) {}
  getRole(): Observable<string> {
    return of(null).pipe(
      map(() => localStorage.getItem('token')),
      map(token => this.authService.isAuthenticated() 
        ? this.jwtHelperService.decodeToken(token)['scope'] 
        : 'guest'
      )
    );
  }
}
