export class AddMedicalRecordRequest {
    bloodType: string;
    heartRate: number;
    description: string;
    patientId: number;
    appointmentId: number;
    constructor() {}
}

export class EditMedicalRecordRequest {
    bloodType: string;
    heartRate: number;
    description: string;
    diagnosis: string;
    allergies: string;
    prescription: string;
    treatmentPlan: string;
    note: string;
    patientId: number;
    medicalRecordId: number;
    constructor() {}
}

export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; 
  }