import {  prop, required, email, unique } from "@rxweb/reactive-form-validators"
import { User } from "./user"
export class Patient{
    @prop({isPrimaryKey:true})
    id:string
    @prop()
    firstName: string
    @prop()
    lastName:string
    @prop()
    fullName: string
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
    avatarFilePath: string

}