import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  const authToken = localStorage.getItem('token');
  if(authToken!=null){
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`,
        
      }
    });
    return next(authReq);
  }
  
  return next(req);
};
