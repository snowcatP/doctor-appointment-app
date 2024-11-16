import { Component } from '@angular/core';
import { Doctor } from '../../../core/models/doctor';
import { DoctorService } from '../../../core/services/doctor.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import {
  date,
  email,
  password,
  RxwebValidators,
} from '@rxweb/reactive-form-validators';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FileUploadErrorEvent, UploadEvent } from 'primeng/fileupload';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import { first } from 'rxjs';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-doctor-table',
  templateUrl: './doctor-table.component.html',
  styleUrl: './doctor-table.component.css',
})
export class DoctorTableComponent {
  doctors: Doctor[] = [];
  listSpecialty: Specialty[];
  addNewDoctorVisible: boolean = false;
  viewDoctorDetailsVisible: boolean = false;
  formAddNewDoctor: FormGroup;
  formEditDoctor: FormGroup;
  selectedDoctors: Doctor[] | null;
  selectedFile: File;
  defaultDate: string;
  specialtyName: string;
  doctorGender: boolean;
  loadingFetchingData: boolean = true;
  constructor(
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private datePipe: DatePipe
  ) {}
  ngOnInit(): void {
    this.getListDoctor();
    this.getListSpecialty();
    this.formAddNewDoctor = this.fb.group({
      firstName: ['', [RxwebValidators.required()]],
      lastName: ['', [RxwebValidators.required()]],
      password: ['', [RxwebValidators.required()]],
      gender: ['', [RxwebValidators.required()]],
      phone: ['', [RxwebValidators.alpha()]],
      email: ['', RxwebValidators.required()],
      avatarFilePath: [
        'assets/images/profile/default-avatar.jpg',
        RxwebValidators.fileSize({
          maxSize: 50,
          conditionalExpression: 'x => x.fileType == "Picture"',
        }),
      ],
      address: [],
      dateOfBirth: ['', RxwebValidators.date()],
      specialtyId: ['', RxwebValidators.required()],
    });
    this.formEditDoctor = this.fb.group({
      firstName: ['', [RxwebValidators.required()]],
      lastName: ['', [RxwebValidators.required()]],
      password: ['', [RxwebValidators.required()]],
      gender: ['', [RxwebValidators.required()]],
      phone: ['', [RxwebValidators.alpha()]],
      email: ['', RxwebValidators.required()],
      avatarFilePath: [
        '',
        RxwebValidators.fileSize({
          maxSize: 50,
          conditionalExpression: 'x => x.fileType == "Picture"',
        }),
      ],
      address: [],
      dateOfBirth: ['', RxwebValidators.date()],
      specialtyId: ['', RxwebValidators.required()],
    });
    
  }
  showAddNewDoctorDialog() {
    this.addNewDoctorVisible = !this.addNewDoctorVisible;
  }
  getListDoctor() {
    const defaultAvatarPath ='https://firebasestorage.googleapis.com/v0/b/doctorappointmentwebapp.appspot.com/o/16971d8f-924a-4dc0-9de4-f2de7db5fe40_default-avatar.jpg?alt=media';
    this.doctorService.getListDoctor().subscribe({
      next: (resp) =>{
        this.doctors = resp.data.map((doctor: Doctor) => {
          // Check if avatarFilePath is null or empty, and set default if needed
          doctor.avatarFilePath = doctor.avatarFilePath ? doctor.avatarFilePath : defaultAvatarPath;
          return doctor;
        });
        this.loadingFetchingData = false;
      }
      
    });
  }
  getListSpecialty() {
    this.specialtyService.getListSpecialty().subscribe((resp) => {
      this.listSpecialty = resp.data;
    });
  }
  onUpload($event: FileUploadErrorEvent) {
    this.selectedFile = $event.files[0];
    this.messageService.add({
      severity: 'info',
      summary: 'Success',
      detail: 'File uploaded successfull',
    });
  }
  addNewDoctor() {
    let doctor: Doctor = {
      firstName: this.formAddNewDoctor.controls['firstName'].value,
      lastName: this.formAddNewDoctor.controls['lastName'].value,
      password: this.formAddNewDoctor.controls['password'].value,
      gender: this.formAddNewDoctor.controls['gender'].value,
      phone: this.formAddNewDoctor.controls['phone'].value,
      email: this.formAddNewDoctor.controls['email'].value,
      dateOfBirth: this.formAddNewDoctor.controls['dateOfBirth'].value,
      address: this.formAddNewDoctor.controls['address'].value,
      specialtyId: this.formAddNewDoctor.controls['specialtyId'].value,
      avatarFilePath: this.formAddNewDoctor.controls['avatarFilePath'].value,
    };

    this.doctorService.addNewDoctor(doctor, this.selectedFile).subscribe({
      next: (res) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Add new doctor successfully',
        });
      },
    });
  }
  openEditDialog(doctor: any){
    this.viewDoctorDetailsVisible = true;
    this.formEditDoctor.patchValue({
      id: doctor.id,
      firstName: doctor.firstName,
      lastName: doctor.lastName,
      gender: doctor.gender,
      phone: doctor.phone,
      email: doctor.email,
      dateOfBirth: doctor.dateOfBirth ,
      address: doctor.address,
      avatarFilePath: doctor.avatarFilePath,
      speciality: doctor.specialty.specialtyName,
      password: doctor.password,

    })
    this.defaultDate = this.datePipe.transform(doctor.dateOfBirth, 'dd-MM-yyyy'),
    this.specialtyName = doctor.specialty.specialtyName,
    this.doctorGender = doctor.gender,
    //console.log(this.formEditDoctor.controls)
    console.log(doctor)
  }
  editDoctorInformation(){

  }
  deleteSelectedDoctor() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected doctors?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.doctors = this.doctors.filter(
          (val) => !this.selectedDoctors?.includes(val)
        );
        this.selectedDoctors = null;
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Doctor Deleted',
        });
      },
    });
  }
}
