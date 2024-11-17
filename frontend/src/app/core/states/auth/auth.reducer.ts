import {
  createFeatureSelector,
  createReducer,
  createSelector,
  on,
} from '@ngrx/store';
import { User } from '../../models/authentication.model';
import { loadProfile, loginFailure, loginSuccess, logout, refreshToken } from './auth.actions';

export interface State {
  isLogged: boolean;
  token: string;
  user: User;
  role: string;
  loginError?: string;
}

export const initialState: State = {
  token: null,
  user: null,
  role: 'GUEST',
  isLogged: false
};

const _authReducer = createReducer(
  initialState,
  on(loginSuccess, (state, { loginSuccessResponse }) => {
    return {
      ...state,
      token: loginSuccessResponse.token,
      user: loginSuccessResponse.user,
      role: loginSuccessResponse.user.role.roleName,
      isLogged: true,
    };
  }),
  on(loginFailure, (state, { error }) => {
    return {
      ...state,
      token: null,
      user: null,
      role: 'GUEST',
      isLogged: false,
      loginError: error,
    };
  }),
  on(loadProfile, (state, { loginSuccessResponse }) => {
    return {
      ...state,
      token: loginSuccessResponse.token,
      user: loginSuccessResponse.user,
      role: loginSuccessResponse.user.role.roleName,
      isLogged: true,
    };
  }),
  on(refreshToken, (state, {refreshTokenResponse}) => {
    return {
      ...state,
      token: refreshTokenResponse.token,
      user: refreshTokenResponse.user,
      role: refreshTokenResponse.user.role.roleName,
      isLogged: true,
    }
  }),
  on(logout, (state, {logoutSuccess}) => {
    return {
      ...state,
      token: null,
      user: null,
      role: 'GUEST',
      loginError: null,
      isLogged: false,
    }
  })
);

export function authReducer(state: any, action: any) {
  return _authReducer(state, action);
}

export const selectAuthState = createFeatureSelector<State>('auth');
export const selectToken = createSelector(
  selectAuthState,
  (state) => state.token
);
export const selectUser = createSelector(
  selectAuthState,
  (state) => state.user
);
export const selectErrorMessage = createSelector(
  selectAuthState,
  (state) => state.loginError
);
export const selectIsLogged = createSelector(
  selectAuthState,
  (state) => state.isLogged
);
export const selectRole = createSelector(
  selectAuthState,
  (state) => state.role
)