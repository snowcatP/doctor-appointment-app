import { Component } from '@angular/core';
import { Doctor } from '../../../core/models/doctor';
import { DoctorService } from '../../../core/services/doctor.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { email, RxwebValidators } from '@rxweb/reactive-form-validators';
import { MessageService } from 'primeng/api';
import { UploadEvent } from 'primeng/fileupload';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';

@Component({
  selector: 'app-doctor-table',
  templateUrl: './doctor-table.component.html',
  styleUrl: './doctor-table.component.css',
})
export class DoctorTableComponent {
  doctors: Doctor[] = [];
  listSpecialty: Specialty[];
  addNewDoctorVisible: boolean = false;
  formAddNewDoctor: FormGroup;
  constructor(
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private fb: FormBuilder,
    private messageService: MessageService
  ) {}
  ngOnInit(): void {
    this.getListDoctor();
    this.getListSpecialty();
    this.formAddNewDoctor = this.fb.group({
      firstName: ['', [RxwebValidators.required()]],
      lastName: ['', [RxwebValidators.required()]],
      gender: ['', [RxwebValidators.required()]],
      phone: ['', [RxwebValidators.digit()]],
      email: ['', RxwebValidators.required()],
      avatarFilePath: [
        '',
        RxwebValidators.fileSize({
          maxSize: 50,
          conditionalExpression: 'x => x.fileType == "Picture"',
        }),
      ],
      dateOfBirth: ['', RxwebValidators.date()],
      specialty: ['', RxwebValidators.required()],
    });
  }
  showAddNewDoctorDialog() {
    console.log(this.addNewDoctorVisible)
    this.addNewDoctorVisible = !this.addNewDoctorVisible;
  }
  getListDoctor() {
    this.doctorService.getListDoctor().subscribe((resp) => {
      this.doctors = resp.data;
    });
  }
  getListSpecialty(){
    this.specialtyService.getListSpecialty().subscribe((resp)=>{
      this.listSpecialty = resp.data;
    })
  }
  onUpload(event: UploadEvent) {
    this.messageService.add({
      severity: 'info',
      summary: 'Success',
      detail: 'File Uploaded with Basic Mode',
    });
  }
}
