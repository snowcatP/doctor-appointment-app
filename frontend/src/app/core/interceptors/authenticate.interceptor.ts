import { HttpInterceptorFn } from '@angular/common/http';
import { jwtDecode } from 'jwt-decode';


function isTokenExpired(token: string): boolean {
  try {
    const { exp } = jwtDecode<{ exp: number }>(token);
    return exp * 1000 < Date.now();
  } catch (error) {
    return true;
  }
}

export const appInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const authToken = localStorage.getItem('token');
  if (authToken != null) {

    if (!isTokenExpired(authToken)) {
      const clonedRequest = req.clone({
        headers: req.headers.set(
          'Authorization',
          `Bearer ${localStorage.getItem('token')}`
        ),
      });
      return next(clonedRequest);
    } else {
      localStorage.removeItem('token');
    }
  }
  return next(req);
};


