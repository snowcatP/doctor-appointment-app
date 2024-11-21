import { Specialty } from './booking.model';

export class DoctorResponse {
  // still missing fields, if need, please add new
  id: number;
  fullName: string;
  email: string;
  specialty: Specialty;
  avatarFilePath: string;
}
export class UpdateDoctorProfileRequest {
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

export class DoctorChatResponse {
  id: number;
  fullName: string;
  email: string;
  specialty: Specialty;
  avatarFilePath: string;
}