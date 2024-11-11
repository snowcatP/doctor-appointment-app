import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Store } from '@ngrx/store';

export const appInterceptorInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const authToken = localStorage.getItem('token');
  const authService = inject(AuthService);
  let clonedRequest = req;

  const endpointsNoBearer = [
    '/api/auth/refreshToken',
    '/api/auth/logout',
    '/api/auth/login',
  ];

  if (authToken && !endpointsNoBearer.some((e) => req.url.includes(e))) {
    clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    });
  }

  return next(clonedRequest).pipe(
    catchError((error) => {
      if (error.status === 401 || error.status === 403) {
        return authService.refreshToken().pipe(
          switchMap(() => {
            const refreshedToken = localStorage.getItem('token');
            const newAuthReq = req.clone({
              setHeaders: { Authorization: `Bearer ${refreshedToken}` },
            });
            return next(newAuthReq);
          })
        );
      }
      return throwError(error);
    })
  );
};
