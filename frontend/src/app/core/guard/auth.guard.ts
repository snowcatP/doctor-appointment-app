import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  return authService.isAuthenticated().pipe(
    map((isAuthenticated: boolean) => {
      const requiredPermission = route.data?.permission;
      if (!isAuthenticated) {
        router.navigate(['/auth']);
        return false;
      }
      if (
        requiredPermission &&
        !authService.hasPermission(requiredPermission)
      ) {
        router.navigate(['/error404']);
        return false;
      }
      return true;
    })
  );
};
