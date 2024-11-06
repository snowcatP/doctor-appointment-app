import {
  createFeatureSelector,
  createReducer,
  createSelector,
  on,
} from '@ngrx/store';
import { User } from '../../models/authentication.model';
import { loginFailure, loginSuccess, logout, refreshToken } from './auth.actions';

export interface State {
  token: string;
  user: User;
  loginError?: string;
}

export const initialState: State = {
  token: null,
  user: null,
};

const _authReducer = createReducer(
  initialState,
  on(loginSuccess, (state, { loginSuccessResponse }) => {
    return {
      ...state,
      token: loginSuccessResponse.token,
      user: loginSuccessResponse.user,
    };
  }),
  on(loginFailure, (state, { error }) => {
    return {
      ...state,
      token: null,
      user: null,
      loginError: error,
    };
  }),
  on(refreshToken, (state, {refreshTokenResponse}) => {
    return {
      ...state,
      token: refreshTokenResponse.token,
      user: refreshTokenResponse.user
    }
  }),
  on(logout, (state, {logoutSuccess}) => {
    return {
      ...state,
      token: null,
      user: null,
      loginError: null
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
