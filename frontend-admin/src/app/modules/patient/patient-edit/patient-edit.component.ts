import { DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import {
  ConfirmationService,
  MessageService,
  PrimeNGConfig,
} from 'primeng/api';
import { FileUploadErrorEvent } from 'primeng/fileupload';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../core/models/patient';

@Component({
  selector: 'app-patient-edit',
  templateUrl: './patient-edit.component.html',
  styleUrl: './patient-edit.component.css',
})
export class PatientEditComponent implements OnInit {
  defaultDate: any;
  selectedFile: File | null = null;
  formEditPatient: FormGroup;
  totalSize: number = 0;
  totalSizePercent: number = 0;
  patient: Patient;

  constructor(
    private config: PrimeNGConfig,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private patientService: PatientService
  ) {}
  ngOnInit(): void {
    if(this.route.snapshot.queryParamMap.get('id')!==null){
      this.route.queryParams.subscribe((params)=>{
        this.getPatientDetail(params['id']);
      })
    }
    else{
      this.getTheFirstPatinet();
    }
    this.formEditPatient = this.fb.group({
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

  getPatientDetail(id: number){  
    this.patientService.getPatientDetail(id).subscribe({
      next: (resp) =>{
        console.log(resp)
        this.formEditPatient.patchValue({
          id: resp.data.id,
          firstName: resp.data.firstName,
          lastName: resp.data.lastName,
          gender: resp.data.gender,
          phone: resp.data.phone,
          email: resp.data.email,
          dateOfBirth: resp.data.dateOfBirth,
          address: resp.data.address,
          avatarFilePath: resp.data.avatarFilePath,
          password: resp.data.password,
        });
        this.patient = resp.data;
        this.cdr.detectChanges();
        this.defaultDate = this.datePipe.transform(resp.data.dateOfBirth,'dd-MM-yyyy');
      }
    })

  }
  getTheFirstPatinet() {
    this.patientService.getListPatient().subscribe({
      next: (resp) => {
        this.getPatientDetail(resp[0].id);
      },
    });
  }

  editPatient() {
    let patientInfoForm: Patient = {
      id: this.formEditPatient.controls['id'].value,
      firstName: this.formEditPatient.controls['firstName'].value,
      lastName: this.formEditPatient.controls['lastName'].value,
      fullName : '',
      password: this.formEditPatient.controls['password'].value,
      gender: this.formEditPatient.controls['gender'].value,
      phone: this.formEditPatient.controls['phone'].value,
      email: this.formEditPatient.controls['email'].value,
      dateOfBirth: this.formEditPatient.controls['dateOfBirth'].value,
      address: this.formEditPatient.controls['address'].value,
      avatarFilePath: this.formEditPatient.controls['avatarFilePath'].value,
    };
    console.log(patientInfoForm)
    this.patientService.editPatient(
        patientInfoForm,
        this.selectedFile,
        this.formEditPatient.controls['id'].value
      )
      .subscribe({
        next: (res) => {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: `Edit Patient ${this.formEditPatient.controls['firstName'].value + ' ' + this.formEditPatient.controls['lastName'].value} successfully`,
          });

        },
        error: (res) =>{
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: `Edit Patient ${this.formEditPatient.controls['firstName'].value + ' ' + this.formEditPatient.controls['lastName'].value} unsuccessfully`,
          });
        }
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
}
