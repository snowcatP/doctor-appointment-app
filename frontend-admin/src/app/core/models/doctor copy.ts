import {  email, prop, required } from "@rxweb/reactive-form-validators"
import { Specialty } from "./speciality"

export class DoctorResponse {
    id: number;
    fullName: string;
    email: string;
    specialty: Specialty;
    avatarFilePath: string;
  }
export class Doctor{
    @prop()
    id:number
    @prop()
    firstName:string
    @prop()
    lastName:string
  
    @prop()
    gender:boolean
    @prop()
    phone:string
    @prop()
    password:string
    @email()
    email:string
    @prop()
    dateOfBirth:Date
    @prop()
    address:string
    @prop()
    specialtyId: string
    @prop()
    avatarFilePath: string
   
}   