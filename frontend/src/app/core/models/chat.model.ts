import { DoctorChatResponse, DoctorResponse } from "./doctor.model";
import { PatientChatResponse } from "./patient.model";

export class ChatMessageRequest {
    conversationId?: number;
    message: string;
    doctorEmail: string;
    patientEmail: string;
    sender: string;
}

export class ChatMessageResponse {
    conversationId: string;
    conversation: ConversationResponse;
    message: string;
    timeStamp: Date;
    sender: string;
}

export class CreateConversationRequest {
    doctorEmail: string;
    patientEmail: string;
}

export class ConversationResponse {
    id: number;
    doctor: DoctorChatResponse;
    patient: PatientChatResponse;
}