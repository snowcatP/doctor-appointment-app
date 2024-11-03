import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const route = inject(Router)
  const authService = inject(AuthService)
  const messageService = inject(MessageService)
  const authToken = localStorage.getItem('token'); 
  if(authService.isAuthenticated()){
    const clonedRequest = req.clone({
      setHeaders:{
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json'
      }
    });
    return next(clonedRequest);
  }else{ //if token is expired, so we need to remove it
      localStorage.removeItem('token')
      route.navigateByUrl('')
  }
  return next(req);
};
