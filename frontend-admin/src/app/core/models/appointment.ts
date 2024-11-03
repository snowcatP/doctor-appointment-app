import {  email, prop, required, unique } from "@rxweb/reactive-form-validators"
import { AppointmentStatus } from "./appointment-status"
import { Patient } from "./patient"
import { Doctor } from "./doctor"
export class Appointment{
    @prop({isPrimaryKey:true})
    appoinmentId: string
    @prop()
    fullName: string
    @prop()
    gender: boolean
    @prop()
    phone: string
    @email()
    @unique()
    @required()
    email: string
    @prop()
    dateOfBirth: Date
    @prop()
    reason: string
    @prop()
    dateBooking: Date
    @prop()
    appointmentStatus: AppointmentStatus
    @prop()
    patient: Patient
    @prop()
    doctor: Doctor
}