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

export class Appointment {
  id: number;
  fullName: string;
  phone: string;
  email: string;
  dateOfBirth: Date;
  reason: string;
  dateBooking: Date;
  bookingHour: string;
  appointmentStatus: string;
  doctor: DoctorResponse;
  constructor() {}
}

export class RescheduleAppointment{
  dateBooking: Date;
  bookingHour: string;
}

export class GetAppointmentForReschedulingRequest {
  doctorEmail: string;
}

// export class DoctorResponse {
//   id: number;
//   firstName: string;
//   lastName: string;
//   fullName: string;
//   gender: boolean;
//   phone: string;
//   email: string;
//   dateOfBirth: Date;
//   address: string;
//   avatarFilePath: string;
//   constructor() {}
// }

export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; // Use the actual data type if known, such as `data?: UserProfile`
  }

export class ReferenceCodeRequest{
  referenceCode: string;
}
