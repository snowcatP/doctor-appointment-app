export class UpdateProfileRequest {
    firstName: string;
    lastName: string;
    gender: boolean;
    phone: string;
    email: string;
    dateOfBirth: Date;
    address: string;
    constructor() {}
}

export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; // Use the actual data type if known, such as `data?: UserProfile`
  }
  