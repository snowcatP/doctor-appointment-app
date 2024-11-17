import {  prop, required } from "@rxweb/reactive-form-validators"
export class Specialty{
    @prop()
    id: string
    @prop()
    specialtyName: string
}