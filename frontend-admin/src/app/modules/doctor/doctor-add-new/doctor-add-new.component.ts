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
  selector: 'app-doctor-add-new',
  templateUrl: './doctor-add-new.component.html',
  styleUrl: './doctor-add-new.component.css'
})
export class DoctorAddNewComponent {
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
  addNewDoctor() {
    let doctor: Doctor = {
      id: null,
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
    this.loadingFetchingData = true;

    this.doctorService.addNewDoctor(doctor, this.selectedFile).subscribe({
      next: (res) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Add new Doctor successfully',
        });
        this.formAddNewDoctor.reset();
      },
    });
  }
  
 
}
