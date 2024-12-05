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
import {
  ConfirmationService,
  MessageService,
  PrimeNGConfig,
} from 'primeng/api';
import { FileUploadErrorEvent, UploadEvent } from 'primeng/fileupload';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import { first, forkJoin } from 'rxjs';
import { DatePipe } from '@angular/common';
import { Route, Router } from '@angular/router';

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
  loadingFetchingData: boolean = true;
  totalSize: number = 0;
  totalSizePercent: number = 0;
  constructor(
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private datePipe: DatePipe,
    private config: PrimeNGConfig,
    private route: Router
  ) {}
  ngOnInit(): void {
    this.loadingFetchingData = true;
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
    this.route.navigateByUrl('doctor/add-doctor');
  }
  openEditDialog(doctor: any) {
    this.route.navigate(['doctor/edit-doctor'], {queryParams: {id:doctor.id}});
  }
  getListDoctor() {
    const defaultAvatarPath =
      'https://firebasestorage.googleapis.com/v0/b/doctorappointmentwebapp.appspot.com/o/16971d8f-924a-4dc0-9de4-f2de7db5fe40_default-avatar.jpg?alt=media';
    this.doctorService.getListDoctor().subscribe({
      next: (resp) => {
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
      this.listSpecialty = resp;
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
  
  deleteSelectedDoctor(doctor: Doctor) {
    console.log(doctor)
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected doctor?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.doctorService.deleteDoctor(doctor.id.toString()).subscribe({
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
            this.doctorService.deleteDoctor(doctor.id.toString())
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
  getSeverity(gender: boolean) {
    switch (gender) {
      case true:
        return 'success';

      case false:
        return 'warning';

      default:
        return null;
    }
  }
}
