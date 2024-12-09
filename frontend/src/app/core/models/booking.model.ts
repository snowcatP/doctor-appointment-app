export class DoctorBooking {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  address: string;
  schedule: string;
  avatarFilePath: string;
  specialty: Specialty;
  averageRating: number;
  numberOfFeedbacks: number;
  constructor() {}
}

export class AppointmentSlot {
  dayWeek: string;
  date: string;
  timeSlotsMorning: TimeSlot[];
  timeSlotsAfternoon: TimeSlot[];
  constructor(dayWeek: string, date: string, timeSlotsMorning: TimeSlot[], timeSlotsAfternoon: TimeSlot[]) {
    this.dayWeek = dayWeek;
    this.date = date;
    this.timeSlotsMorning = timeSlotsMorning;
    this.timeSlotsAfternoon = timeSlotsAfternoon;
  }
}

export class AppointmentsBooked {
    id: number;
    dateBooking: Date;
    bookingHour: string;
    constructor() {}
}

export class Specialty {
  id: number;
  specialtyName: string;
  constructor() {}
}

interface IBookingDataGuest {
  doctorId: number;
  doctorName: string
  fullName: string;
  phone: string;
  email: string;
  reason: string;
  dateBooking: Date;
  bookingHour: string;
}

interface IBookingDataPatient {
  doctorId: number;
  doctorName: string
  patientId: number;
  reason: string;
  dateBooking: Date;
  bookingHour: string;
  doctor: DoctorBooking;
}
export class BookingDataPatient {
  constructor(bookingData?: IBookingDataPatient) {
    this.doctorId = bookingData?.doctorId;
    this.doctorName = bookingData?.doctorName;
    this.patientId = bookingData?.patientId;
    this.reason = bookingData?.reason;
    this.dateBooking = bookingData?.dateBooking;
    this.bookingHour = bookingData?.bookingHour;
    this.doctor = bookingData?.doctor;
  }
  doctorId: number;
  doctorName: string
  patientId: number;
  reason: string;
  dateBooking: Date;
  bookingHour: string;
  doctor?: DoctorBooking;
}

export class BookingDataGuest {
  constructor(bookingData?: IBookingDataGuest) {
    this.doctorId = bookingData?.doctorId;
    this.doctorName = bookingData?.doctorName;
    this.reason = bookingData?.reason;
    this.fullName = bookingData?.fullName;
    this.email = bookingData?.email;
    this.phone = bookingData?.phone;
    this.dateBooking = bookingData?.dateBooking;
    this.bookingHour = bookingData?.bookingHour;
  }
  doctorId: number;
  doctorName: string
  reason: string;
  fullName: string;
  phone: string;
  email: string;
  dateBooking: Date;
  bookingHour: string;
}

export class TimeSlot {
  time: string;
  date: Date;
  isBooked: boolean;
  isPassedIn: boolean;
  constructor(time: string, date: Date) {
    this.time = time;
    this.date = date;
    this.isBooked = false;
    this.isPassedIn = false;
  }
}

