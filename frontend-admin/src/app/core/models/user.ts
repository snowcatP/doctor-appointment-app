import {  prop, required, email, unique } from "@rxweb/reactive-form-validators"
export class User {
    @prop({isPrimaryKey: true})
    userId: string

    @prop()
    @required()
    firstName: string

    @prop()
    @required()
    lastName: string

    @prop()
    gender: boolean

    @prop()
    phone: string

    @email()
    @required()
    @unique()
    email: string

    @prop()
    password: string

    @prop()
    dateOfBirth: Date
    
    @prop()
    address: string

    @prop()
    roleId:string

    @prop({defaultValue:true})
    isActive: boolean
}