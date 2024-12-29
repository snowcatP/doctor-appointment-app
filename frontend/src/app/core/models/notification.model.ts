import { AppointmentResponse, AppointmentStatus } from "./appointment.model";
import { DoctorBooking } from "./booking.model";

export class BookingNotification {
    appointmentId: number;
    doctor: DoctorBooking;
    dateBooking: Date;
    bookingHour: string;
    constructor() {}
}
export interface AppNotification{
    appointmentId: number;
    status: 'ACCEPT' |'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED'| 'PENDING' | 'RESCHEDULED'
    message: string
}