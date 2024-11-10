import { DoctorBooking } from "./booking.model";

export class BookingNotification {
    appointmentId: number;
    doctor: DoctorBooking;
    dateBooking: Date;
    bookingHour: string;
    constructor() {}
}