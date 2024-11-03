import {  prop, required, email, unique } from "@rxweb/reactive-form-validators"
import { Patient } from "./patient"
import { Doctor } from "./doctor"
export class MedicalRecord{
    @prop({isPrimaryKey: true})
    medicalRecordId: string
    @prop()
    @required()
    description: string
    @prop()
    dateCreated: Date     
    @prop()
    lastModified: Date  
    @prop()
    patient: Patient
    @prop()
    doctorModified: Doctor
}