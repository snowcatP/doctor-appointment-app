import {  prop, required } from "@rxweb/reactive-form-validators"
import { Doctor } from "./doctor"
export class Specialty{
    @prop()
    id: number
    @prop()
    specialtyName: string
    @prop()
    headDoctorId: number
    @prop()
    listDoctorId: number[]
}