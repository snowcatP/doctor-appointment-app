export class DoctorBooking {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
    avatarFilePath: string;
    specialty: Specialty;
    constructor() {}
}

export class Specialty {
    id: number;
    specialtyName: string;
    constructor() {}
}

export class BookingData {
    constructor() {}
    specialty: string;
    doctor: string;
    patientfirstName: string;
    patientlastName: string;
    patientEmail: string;
    patientPhone: string;
    reason: string;
    bookingDate: Date;
    bookingHour: string;
}