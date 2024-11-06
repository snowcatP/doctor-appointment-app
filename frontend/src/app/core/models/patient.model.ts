export class UpdateProfileRequest {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
    gender: boolean;
    phone: string;
    email: string;
    dateOfBirth: Date;
    address: string;
    role: Role;
    constructor() {}
}
export class Role {
    id: number;
    roleName: string;
  }
export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; // Use the actual data type if known, such as `data?: UserProfile`
  }
  