export class User {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  email: string;
  gender: boolean;
  phone: string;
  dateOfBirth: Date;
  address: string;
  avatarFilePath: string;
  role: Role;
}

export class Role {
  id: number;
  roleName: string;
}

export class LoginRequest {
  email: string;
  password: string;
}

export class RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  gender: boolean;
  password: string;
  confirmPassword: string;
}

export class LoginSucessResponse {
  isAuthenticated: boolean;
  token: string;
  user: User;
  constructor(token: string, user: User) {
    this.token = token;
    this.user = user;
  }
}

export class UserChangePasswordRequest {
  password: string;
  confirmPassword: string;
}
