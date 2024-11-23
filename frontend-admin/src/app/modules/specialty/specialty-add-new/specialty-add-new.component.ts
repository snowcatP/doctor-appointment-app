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

@Component({
  selector: 'app-specialty-add-new',
  templateUrl: './specialty-add-new.component.html',
  styleUrl: './specialty-add-new.component.css',
})
export class SpecialtyAddNewComponent implements OnInit {

  doctors: Doctor[] | undefined;
  selectedDoctor: Doctor;
  selectedListDoctor: Doctor[];
  selectedListDoctorId: number[] = [];
  filterValue: string | undefined = '';
  formAddSpecialty: FormGroup;
  label: any;
  constructor(
    private specialtyService: SpecialtyService,
    private doctorService: DoctorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private config: PrimeNGConfig,
    private fb: FormBuilder
  ) {}
  ngOnInit(): void {
    this.getListDoctor();
    this.formAddSpecialty = this.fb.group({
      specialtyName: ['', RxwebValidators.required()],
      specialtyHeadDoctor: ['', RxwebValidators.required()],
      specialtyListDoctors: ['', RxwebValidators.required()],
    })

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
        })
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
    event.filteredValue = this.doctors.filter((doctor) =>
      doctor.firstName.toLowerCase().includes(query) || doctor.lastName.toLowerCase().includes(query) || doctor.id.toString().includes(query)
    );
  }
  addNewSpecialty() {
    console.log(this.selectedListDoctor)
    const listDoctorId = this.selectedListDoctor.map(listDoctor =>
       listDoctor.id
    )
    let newSpecialty: Specialty = {
      id: null,
      specialtyName: this.formAddSpecialty.controls['specialtyName'].value,
      headDoctorId: this.formAddSpecialty.controls['specialtyHeadDoctor'].value.id,
      listDoctorId: listDoctorId
  
    };
    this.specialtyService.addNewSpecialty(newSpecialty).subscribe({
      next: (res) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Add new Specialty successfully',
        });
        this.formAddSpecialty.reset();
      }, 
  
    })
  }
}
