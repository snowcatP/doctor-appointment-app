import {  prop, required } from "@rxweb/reactive-form-validators"
export class Specialty{
    @prop()
    id: number
    @prop()
    specialtyName: string
}