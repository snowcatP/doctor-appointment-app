import {  prop, required, email, unique } from "@rxweb/reactive-form-validators"
import { User } from "./user"
export class Patient{
    @prop({isPrimaryKey:true})
    patientId:string
    @prop()
    profile: User

}