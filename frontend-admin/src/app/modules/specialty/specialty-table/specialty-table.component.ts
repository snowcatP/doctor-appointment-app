import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import {
  ConfirmationService,
  MessageService,
  PrimeNGConfig,
} from 'primeng/api';
import { Doctor } from '../../../core/models/doctor';
import { DoctorService } from '../../../core/services/doctor.service';
import { forkJoin } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-specialty-table',
  templateUrl: './specialty-table.component.html',
  styleUrl: './specialty-table.component.css',
})
export class SpecialtyTableComponent implements OnInit {

  specialties: Specialty[];
  doctors: Doctor[];
  loadingFetchingData: boolean = true;
  formAddNewSpecialty: FormGroup;
  addNewSpecialtyVisible: boolean = false;
  selectedSpecialty: Specialty[];
  viewSpecilatyDetailsVisible: any;
  formEditDoctor: FormGroup<any>;
  constructor(
    private specialtyService: SpecialtyService,
    private doctorService: DoctorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private config: PrimeNGConfig,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.getListSpecialty();

  }
  getListSpecialty() {
    this.specialtyService.getListSpecialty().subscribe({
      next: (resp) => {
      this.specialties = resp;
      this.loadingFetchingData = false;
    }});
  }
  
  showAddSpecialty() {
    this.router.navigateByUrl('specialty/add-specialty');
  }
  addNewSpecialty() {}
  editSpecialty() {}
  deleteMultipleSpecialty() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected specialties?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (this.selectedSpecialty != null) {
          const deleteRequests = this.selectedSpecialty.map((specialty) =>
            this.specialtyService.deleteSpecialty(specialty.id)
          );
          forkJoin(deleteRequests).subscribe({
            next: () => {
              this.messageService.add({
                key: 'messageToast',
                severity: 'success',
                summary: 'Success',
                detail: 'All selected specialties have been deleted successfully.',
              });
              this.getListSpecialty();
            },
            error: (error) => {
              console.error('Error occurred while deleting doctors:', error);
            },
          });
        }
      },
    });
  }
  deleteSelectedSpecialty(specialty: Specialty) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected specialty?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.specialtyService.deleteSpecialty(specialty.id).subscribe({
          next: (res) => {
            this.messageService.add({
              key: 'messageToast',
              severity: 'success',
              summary: 'Success',
              detail: `Delete Specialy '${specialty.specialtyName}' successfully`,
            });
            this.getListSpecialty();
          },
        });
      },
    });
  }
  openEditDialog(specialty: Specialty) {
    this.router.navigate(['specialty/edit-specialty'], { queryParams: { id: specialty.id} });
  }
}
