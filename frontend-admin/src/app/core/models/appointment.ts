import { email, prop, required, unique } from '@rxweb/reactive-form-validators';
import { AppointmentStatus } from './appointment-status';
import { Patient } from './patient';
import { Doctor, DoctorResponse } from './doctor';
import { Specialty } from './speciality';
import { MedicalRecordResponse } from './medical-record';
export interface AppointmentResponse {
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
  @prop({ isPrimaryKey: true })
  id: number;
  @prop()
  fullName: string;
  @prop()
  gender: boolean;
  @prop()
  phone: string;
  @email()
  @unique()
  @required()
  email: string;
  @prop()
  patientId: number;
  @prop()
  patientBirthday: string;
  @prop()
  reason: string;
  @prop()
  dateBooking: string;
  @prop()
  bookingHour: string;
  @prop()
  appointmentStatus: string;
  @prop()
  patient: Patient;
  @prop()
  doctor: Doctor;
  @prop()
  specialty: Specialty;
  
}
