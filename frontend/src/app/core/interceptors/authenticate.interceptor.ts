import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

export const appInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const messageService = inject(MessageService);
  const router = inject(Router)
  if(authService.isAuthenticated()){
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${localStorage.getItem('token')}`)
    });
    return next(clonedRequest);
  }
  else{
    localStorage.removeItem('token')
    router.navigateByUrl('/auth/login')
    messageService.add({key:'messageToast' ,severity: 'error',summary:'Error', detail: `Your session is expired, please login again!` })
  }
  return next(req);
};
