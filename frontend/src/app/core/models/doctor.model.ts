import { Specialty } from "./booking.model";

export class DoctorResponse {
    // still missing fields, if need, please add new
    id: number;
    fullName: string;
    email: string;
    specialty: Specialty
}