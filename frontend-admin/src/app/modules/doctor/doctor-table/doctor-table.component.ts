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
import { ConfirmationService, MessageService, PrimeNGConfig } from 'primeng/api';
import { FileUploadErrorEvent, UploadEvent } from 'primeng/fileupload';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import { first, forkJoin } from 'rxjs';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-doctor-table',
  templateUrl: './doctor-table.component.html',
  styleUrl: './doctor-table.component.css',
})
export class DoctorTableComponent {
  doctors: Doctor[] = [];
  doctorAverageRating: number;
  doctorNumberOfFeedbacks: number;
  doctorFirstName: string;
  doctorLastName: string;
  doctorSpecialty: string;
  listSpecialty: Specialty[];
  addNewDoctorVisible: boolean = false;
  viewDoctorDetailsVisible: boolean = false;
  formAddNewDoctor: FormGroup;
  formEditDoctor: FormGroup;
  selectedDoctors: Doctor[] | null;
  selectedFile: File | null = null;
  defaultDate: string;
  specialtyName: string;
  loadingFetchingData: boolean;
  totalSize : number = 0;
  totalSizePercent : number = 0;
  constructor(
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private datePipe: DatePipe,
    private config: PrimeNGConfig
  ) {}
  ngOnInit(): void {
    this.loadingFetchingData= true;
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
    this.formEditDoctor = this.fb.group({
      id: [''],
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
    const defaultAvatarPath =
      'https://firebasestorage.googleapis.com/v0/b/doctorappointmentwebapp.appspot.com/o/16971d8f-924a-4dc0-9de4-f2de7db5fe40_default-avatar.jpg?alt=media';
    this.doctorService.getListDoctor().subscribe({
      next: (resp) => {
        console.log(resp);
        this.doctors = resp.map((doctor: Doctor) => {
          // Check if avatarFilePath is null or empty, and set default if needed
          doctor.avatarFilePath = doctor.avatarFilePath ?? defaultAvatarPath;
          return doctor;
        });
        this.loadingFetchingData = false;
      },
    });
  }
  getListSpecialty() {
    this.specialtyService.getListSpecialty().subscribe((resp) => {
      this.listSpecialty = resp.data;
    });
  }
  formatSize(bytes: any) {
    const k = 1024;
    const dm = 3;
    const sizes = this.config.translation.fileSizeTypes;
    if (bytes === 0) {
        return `0 ${sizes[0]}`;
    }

    const i = Math.floor(Math.log(bytes) / Math.log(k));
    const formattedSize = parseFloat((bytes / Math.pow(k, i)).toFixed(dm));

    return `${formattedSize} ${sizes[i]}`;
}
  onUpload($event: FileUploadErrorEvent) {
    this.selectedFile = $event.files[0];
    this.totalSize += parseInt(this.formatSize(this.selectedFile.size));
    this.totalSizePercent = this.totalSize / 10;

    this.messageService.add({
      severity: 'info',
      summary: 'Success',
      detail: 'File uploaded successfull',
    });
  }
  addNewDoctor() {
    let doctor: Doctor = {
      id: '',
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
          detail: 'Add new Doctor successfully',
        });
        this.formAddNewDoctor.reset();
        this.getListDoctor();
      },
    });
  }
  openEditDialog(doctor: any) {
    this.viewDoctorDetailsVisible = true;
    this.formEditDoctor.patchValue({
      id: doctor.id,
      firstName: doctor.firstName,
      lastName: doctor.lastName,
      gender: doctor.gender,
      phone: doctor.phone,
      email: doctor.email,
      dateOfBirth: doctor.dateOfBirth,
      address: doctor.address,
      avatarFilePath: doctor.avatarFilePath,
      specialtyId: doctor.specialty.id,
      password: doctor.password,
    });
    console.log(doctor.password);
    this.doctorAverageRating = doctor.averageRating;
    this.doctorNumberOfFeedbacks = doctor.numberOfFeedbacks;
    this.defaultDate = this.datePipe.transform(doctor.dateOfBirth,'dd-MM-yyyy');
    this.specialtyName = doctor.specialty.specialtyName;
    this.doctorFirstName = doctor.firstName;
    this.doctorLastName = doctor.lastName;
  }
  editDoctor() {
    let doctorInfoForm: Doctor = {
      id: this.formEditDoctor.controls['id'].value,
      firstName: this.formEditDoctor.controls['firstName'].value,
      lastName: this.formEditDoctor.controls['lastName'].value,
      password: this.formEditDoctor.controls['password'].value,
      gender: this.formEditDoctor.controls['gender'].value,
      phone: this.formEditDoctor.controls['phone'].value,
      email: this.formEditDoctor.controls['email'].value,
      dateOfBirth: this.formEditDoctor.controls['dateOfBirth'].value,
      address: this.formEditDoctor.controls['address'].value,
      specialtyId: this.formEditDoctor.controls['specialtyId'].value,
      avatarFilePath: this.formEditDoctor.controls['avatarFilePath'].value,
    };
    console.log(doctorInfoForm);
    this.loadingFetchingData = true;
    this.doctorService.editDoctor(
        doctorInfoForm,
        this.selectedFile,
        this.formEditDoctor.controls['id'].value
      )
      .subscribe({
        next: (res) => {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: `Edit Doctor ${this.formEditDoctor.controls['firstName'].value} successfully`,
          });
          this.getListDoctor();
        },
        error: (res) =>{
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: `Edit Doctor ${this.formEditDoctor.controls['firstName'].value} unsuccessfully`,
          });
          this.getListDoctor();
        }
      });
  }
  deleteSelectedDoctor(doctor: Doctor) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected doctor?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.doctorService.deleteDoctor(doctor.id).subscribe({
          next: (res) => {
            this.messageService.add({
              key: 'messageToast',
              severity: 'success',
              summary: 'Success',
              detail: `Delete Doctor '${doctor.firstName}' successfully`,
            });
            this.getListDoctor();
          },
        });
      },
    });
  }
  deleteMultipleDoctor() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected doctors?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (this.selectedDoctors != null) {
          const deleteRequests = this.selectedDoctors.map((doctor) =>
            this.doctorService.deleteDoctor(doctor.id)
          );
          forkJoin(deleteRequests).subscribe({
            next: () => {
              this.messageService.add({
                key: 'messageToast',
                severity: 'success',
                summary: 'Success',
                detail: 'All selected doctors have been deleted successfully.',
              });
              this.getListDoctor();
            },
            error: (error) => {
              console.error('Error occurred while deleting doctors:', error);
            },
          });
        }
      },
    });
  }
}
