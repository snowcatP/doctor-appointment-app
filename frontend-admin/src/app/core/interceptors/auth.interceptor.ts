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
  const isUploadEndpoint = req.url.includes('https://www.primefaces.org/cdn/api/upload.php');
  if(authService.isAuthenticated()&&!isUploadEndpoint){
    const clonedRequest = req.clone({
      setHeaders:{
        'Authorization': `Bearer ${authToken}`,
      }
    });
    return next(clonedRequest);
  }else if(!isUploadEndpoint){ //if token is expired, so we need to remove it
      localStorage.removeItem('token')
      route.navigateByUrl('')
  }
  return next(req);
};
