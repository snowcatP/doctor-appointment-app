import {  email, prop, required } from "@rxweb/reactive-form-validators"
import { Specialty } from "./speciality"

export class Doctor{
    @prop({ isPrimaryKey: true})
    id: number
    @prop()
    firstName:string
    @prop()
    lastName:string
    @prop()
    gender:boolean
    @prop()
    phone:string
    @email()
    email:string
    @prop()
    dateOfBirth:string
    @prop()
    address:string
    @prop()
    specialty: Specialty
    @prop()
    avatarFilePath: string
}   