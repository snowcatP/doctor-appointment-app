import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Doctor } from '../../../core/models/doctor';
import { DropdownFilterOptions } from 'primeng/dropdown';
import { SpecialtyService } from '../../../core/services/specialty.service';
import {
  ConfirmationService,
  MessageService,
  PrimeNGConfig,
} from 'primeng/api';
import { DoctorService } from '../../../core/services/doctor.service';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { Specialty } from '../../../core/models/speciality';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-specialty-edit',
  templateUrl: './specialty-edit.component.html',
  styleUrl: './specialty-edit.component.css',
})
export class SpecialtyEditComponent implements OnInit {
  doctors: Doctor[] = [];
  doctor: Doctor;
  specialty: Specialty;
  specialties: Specialty[];
  selectedDoctor: Doctor;
  selectedListDoctor: Doctor[] = [];
  selectedListDoctorId: number[] = [];
  filterValue: string | undefined = '';
  formEditSpecialty: FormGroup;
  headDoctor: any;
  specialtyId: number;
  constructor(
    private specialtyService: SpecialtyService,
    private doctorService: DoctorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private config: PrimeNGConfig,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    if(this.route.snapshot.queryParamMap.get('id')!=null){
      this.route.queryParams.subscribe((params) => {
        this.specialtyId = params['id'];
      });
      this.getSpecialtyDetail(this.specialtyId);
    }else{
      this.getTheFirtsSpecialty();
    }
    this.getListDoctor();
    
    this.formEditSpecialty = this.fb.group({
      specialtyName: ['', RxwebValidators.required()],
      specialtyHeadDoctor: ['', RxwebValidators.required()],
      specialtyListDoctors: ['', RxwebValidators.required()],
    });
  }
  getTheFirtsSpecialty() {
    this.specialtyService.getListSpecialty().subscribe({
      next: (resp) =>{
        this.getSpecialtyDetail(resp[0].id);
      }
    }
  );
  }
  getSpecialtyDetail(id: any) {
    const defaultAvatarPath =
      'https://firebasestorage.googleapis.com/v0/b/doctorappointmentwebapp.appspot.com/o/16971d8f-924a-4dc0-9de4-f2de7db5fe40_default-avatar.jpg?alt=media';
    this.specialtyService.getSpecialtyDetail(id).subscribe({
      next: (resp) => {
        this.formEditSpecialty.patchValue({
          specialtyName: resp.data.specialtyName,
        });
        this.headDoctor = resp.data.headDoctor;
        this.selectedListDoctor = resp.data.doctorList;
        console.log(resp.data)
      },
    });
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
        console.log(resp)
      },
    });
  }
  resetFunction(options: DropdownFilterOptions) {
    options.reset();
    this.filterValue = '';
  }

  customFilterFunction(event: KeyboardEvent, options: DropdownFilterOptions) {
    options.filter(event);
  }
  customFilter(event: any): void {
    const query = event.filter.trim().toLowerCase(); // User's input
    event.filteredValue = this.doctors.filter(
      (doctor) =>
        doctor.firstName.toLowerCase().includes(query) ||
        doctor.lastName.toLowerCase().includes(query) ||
        doctor.id.toString().includes(query)
    );
  }
  editSpecialty() {
    console.log(  this.formEditSpecialty.controls['specialtyHeadDoctor'].value.id);
    const listDoctorId = this.selectedListDoctor.map(
      (listDoctor) => listDoctor.id
    );
    let editSpecialty: Specialty = {
      id: null,
      specialtyName: this.formEditSpecialty.controls['specialtyName'].value,
      headDoctorId:
        this.formEditSpecialty.controls['specialtyHeadDoctor'].value.id,
      listDoctorId: listDoctorId,
    };
    this.specialtyService
      .editSpecialty(this.specialtyId, editSpecialty)
      .subscribe({
        next: (res) => {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: `Edit Specialty successfully${editSpecialty.specialtyName}`,
          });
          this.formEditSpecialty.reset();
        },
      });
  }
}
