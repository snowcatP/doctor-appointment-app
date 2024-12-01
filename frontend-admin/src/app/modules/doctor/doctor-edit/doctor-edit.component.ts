import { ChangeDetectorRef, Component } from '@angular/core';
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
import { ActivatedRoute, Route, Router } from '@angular/router';

@Component({
  selector: 'app-doctor-edit',
  templateUrl: './doctor-edit.component.html',
  styleUrl: './doctor-edit.component.css'
})
export class DoctorEditComponent {
  doctor: Doctor[];
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
  specialty: Specialty;
  constructor(
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private datePipe: DatePipe,
    private config: PrimeNGConfig,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}
  ngOnInit(): void {
    if(this.route.snapshot.queryParamMap.get('id')!==null){
      this.route.queryParams.subscribe((params)=>{
        this.getDoctorDetail(params['id']);
      })
    }
    else{
      this.getTheFirstDoctor();
    }
    this.getListSpecialty();
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
  
  getDoctorDetail(id: number) {
    this.doctorService.getDoctorDetail(id).subscribe({
      next: (resp) => {
        this.formEditDoctor.patchValue({
          id: resp.id,
          firstName: resp.data.firstName,
          lastName: resp.data.lastName,
          gender: resp.data.gender,
          phone: resp.data.phone,
          email: resp.data.email,
          dateOfBirth: resp.data.dateOfBirth,
          address: resp.data.address,
          avatarFilePath: resp.data.avatarFilePath,
          password: resp.data.password,
          specialtyId: resp.data.specialty.id,
        });
        this.doctorAverageRating = resp.data.averageRating
        this.doctorNumberOfFeedbacks = resp.data.numberOfFeedbacks;
        this.specialty = resp.data.specialty;
        this.specialtyName = resp.data.specialty.specialtyName;
        this.doctorFirstName = resp.data.firstName;
        this.doctorLastName = resp.data.lastName;
        this.defaultDate = this.datePipe.transform(resp.data.dateOfBirth,'dd-MM-yyyy');
        this.cdr.detectChanges();
        console.log(this.specialtyName)
      },
    });
  }
  getTheFirstDoctor() {
    this.doctorService.getListDoctor().subscribe({
      next: (resp) => {
        this.getDoctorDetail(resp[0].id);
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
    console.log(doctorInfoForm)

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
            detail: `Edit Doctor ${this.formEditDoctor.controls['firstName'].value + ' ' + this.formEditDoctor.controls['lastName'].value} successfully`,
          });
          this.loadingFetchingData = false;

        },
        error: (res) =>{
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: `Edit Doctor ${this.formEditDoctor.controls['firstName'].value + ' ' + this.formEditDoctor.controls['lastName'].value} unsuccessfully`,
          });
        }
      });
  }
}
