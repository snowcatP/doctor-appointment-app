export class AddMedicalRecordRequest {
    description: string;
    patientId: number;
    appointmentId: number;
    constructor() {}
}

export class EditMedicalRecordRequest {
    description: string;
    patientId: number;
    medicalRecordId: number;
    constructor() {}
}

export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; 
  }