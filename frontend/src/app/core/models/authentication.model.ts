export class User {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  gender: boolean;
  phone: string;
  dateOfBirth: Date;
  address: string;
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
}
