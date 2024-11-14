import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import * as AuthActions from './auth.actions';
import { catchError, exhaustMap, map, of, tap } from 'rxjs';
import { MessageService } from 'primeng/api';

@Injectable()
export class AuthEffect {
  loginRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginRequest),
      exhaustMap((action) =>
        this.authService.onLogin(action.credential).pipe(
          map((response) =>
            AuthActions.loginSuccess({ loginSuccessResponse: response })
          ),
          catchError((error) => of(AuthActions.loginFailure({ error })))
        )
      )
    )
  );

  loadProfile$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loadProfile),
        tap(({ loginSuccessResponse }) => {
          AuthActions.loadProfile({ loginSuccessResponse });
        })
      ),
    { dispatch: false }
  );
  loginSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginSuccess),
        tap(({ loginSuccessResponse }) => {
          if (loginSuccessResponse.token) {
            localStorage.setItem('token', loginSuccessResponse.token);
          }
        })
      ),
    { dispatch: false }
  );

  refreshToken$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.refreshToken),
        tap(({ refreshTokenResponse }) => {
          if (refreshTokenResponse.token) {
            localStorage.setItem('token', refreshTokenResponse.token);
            AuthActions.refreshToken({ refreshTokenResponse });
          }
        })
      ),
    { dispatch: false }
  );

  logout$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logout),
        tap(({ logoutSuccess }) => {
          localStorage.removeItem('token');
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: logoutSuccess,
          });
          setTimeout(() => {
            this.router.navigateByUrl('/');
          }, 1500);
        })
      ),
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {}
}
