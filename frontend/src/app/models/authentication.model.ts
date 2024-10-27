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
