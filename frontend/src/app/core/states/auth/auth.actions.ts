import { createAction, props } from '@ngrx/store';
import {
  LoginRequest,
  LoginSucessResponse,
} from '../../models/authentication.model';

export const loginRequest = createAction(
  '[Auth] Login Request',
  props<{ credential: LoginRequest }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ loginSuccessResponse: LoginSucessResponse }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: string }>()
);

export const loadProfile = createAction(
  '[Auth] Load Profile',
  props<{ loginSuccessResponse: LoginSucessResponse }>()
)

export const refreshToken = createAction(
  '[Auth] Refresh Token',
  props<{ refreshTokenResponse: LoginSucessResponse }>()
);

export const logout = createAction(
  '[Auth] Logout',
  props<{ logoutSuccess: string }>()
);
