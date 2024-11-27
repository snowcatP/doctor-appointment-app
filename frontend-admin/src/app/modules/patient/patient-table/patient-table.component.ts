import { Component, OnInit } from '@angular/core';
import { Patient } from '../../../core/models/patient';
import { PatientService } from '../../../core/services/patient.service';

@Component({
  selector: 'app-patient-table',
  templateUrl: './patient-table.component.html',
  styleUrl: './patient-table.component.css',
})
export class PatientTableComponent implements OnInit {
  patients: Patient[] = [];
  loadingFetchingData: boolean;
  selectedPatients: Patient[];
  constructor(private patientService: PatientService) {}
  ngOnInit(): void {
    this.getListPatient();
  }
  getListPatient() {
    this.patientService.getListPatient().subscribe({
      next: (resp) => {
        this.patients = resp;
        console.log(this.patients);
      },
    });
  }

  openEditDialog(_t89: any) {
    throw new Error('Method not implemented.');
  }

  deleteMultiplePatient() {}
  showAddNewPatient() {}
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
