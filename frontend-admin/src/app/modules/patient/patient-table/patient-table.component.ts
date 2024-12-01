import { Component, OnInit } from '@angular/core';
import { Patient } from '../../../core/models/patient';
import { PatientService } from '../../../core/services/patient.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-patient-table',
  templateUrl: './patient-table.component.html',
  styleUrl: './patient-table.component.css',
})
export class PatientTableComponent implements OnInit {
  patients: Patient[] = [];
  loadingFetchingData: boolean;
  selectedPatients: Patient[];
  constructor(private patientService: PatientService, private route: Router) {}
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

  openEditDialog(patient:Patient) {
    this.route.navigate(['patient/edit-patient'], {queryParams: {id: patient.id}});
  }

  deleteMultiplePatient() {}
  showAddNewPatient() {
    this.route.navigateByUrl('patient/add-patient');
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
