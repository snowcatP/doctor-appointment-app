import { Component, OnInit } from '@angular/core';
import { Patient } from '../../../core/models/patient';
import { PatientService } from '../../../core/services/patient.service';
import { Router } from '@angular/router';
interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}
@Component({
  selector: 'app-patient-table',
  templateUrl: './patient-table.component.html',
  styleUrl: './patient-table.component.css',
})
export class PatientTableComponent implements OnInit {
  patients: Patient[] = [];
  loadingFetchingData: boolean;
  selectedPatients: Patient[];
  cols!: Column[];

  exportColumns!: ExportColumn[];

  constructor(private patientService: PatientService, private route: Router) {}
  ngOnInit(): void {
    this.getListPatient();
    this.cols = [
      { field: 'id', header: 'Id', customExportHeader: 'Appointment Id' },
      { field: 'firstName', header: 'First Name', customExportHeader: 'First Name' },
      { field: 'lastName', header: 'Last Name', customExportHeader: 'Last Name' },
      { field: 'gender', header: 'Gender', customExportHeader: 'Gender'  },
      { field: 'address', header: 'Address', customExportHeader: 'Address'  },
      { field: 'averageRating', header: 'Rating', customExportHeader: 'Rating'  },
      { field: 'phone', header: 'Phone', customExportHeader: 'Phone'  },
      { field: 'email', header: 'Email', customExportHeader: 'Email'  },
      { field: 'dateOfBirth', header: 'BirthDay', customExportHeader: 'BirthDay'   }
    ];
    this.exportColumns = this.cols.map((col) => ({
      title: col.header,
      dataKey: col.field,
    }));
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
  openPatientDashboard(patient:Patient) {
    this.route.navigate(['patient/dashboard'], {queryParams: {id: patient.id}});
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
