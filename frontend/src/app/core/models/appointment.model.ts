import { DoctorResponse } from "./doctor.model";

export class AppointmentResponse {
    id: number;
    fullName: string;
    phone: string;
    email: string;
    reason: string;
    dateBooking: Date;
    bookingHour: string;
    cusType: string;
    appointmentStatus: string;
    doctor: DoctorResponse;
}