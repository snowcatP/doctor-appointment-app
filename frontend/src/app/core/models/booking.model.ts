export class DoctorBooking {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
    schedule: string;
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

export class DateSchedule {
    constructor() {}
    dayWeek: string;
    date: string;
}