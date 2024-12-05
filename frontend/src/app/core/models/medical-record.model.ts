export class AddMedicalRecordRequest {
    bloodType: string;
    heartRate: number;
    temperature: number;
    weight: number;
    height: number;
    description: string;
    allergies: string;
    patientId: number;
    appointmentId: number;
    constructor() {}
}

export class EditMedicalRecordRequest {
    bloodType: string;
    heartRate: number;
    temperature: number;
    weight: number;
    height: number;
    description: string;
    allergies: string;
    diagnosis: string;
    prescription: string;
    treatmentPlan: string;
    note: string;
    filePath: string;
    patientId: number;
    medicalRecordId: number;
    constructor() {}
}

export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; 
  }