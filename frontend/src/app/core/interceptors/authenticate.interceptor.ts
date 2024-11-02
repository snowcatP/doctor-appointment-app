import { HttpInterceptorFn } from '@angular/common/http';

export const appInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authToken = localStorage.getItem('token');
  if (authToken != null) {
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${localStorage.getItem('token')}`)
    });
    return next(clonedRequest);
  }
  return next(req);
};
