import { prop, required } from '@rxweb/reactive-form-validators';
import { Doctor } from './doctor';
import { Patient } from './patient';
export class Feedback {
    @prop({isPrimaryKey:true})
    feedbackId:string
    @prop()
    comment: string
    @prop()
    dateComment: Date
    @prop()
    rating: string
    @prop()
    doctor: Doctor
    @prop()
    patient: Patient
}
