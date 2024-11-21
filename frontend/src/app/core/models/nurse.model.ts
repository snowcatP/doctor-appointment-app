
export class UpdateNurseProfileRequest {
    firstName: string;
    lastName: string;
    gender: boolean;
    phone: string;
    dateOfBirth: Date;
    address: string;
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
  
