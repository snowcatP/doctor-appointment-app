import { Component } from '@angular/core';
import { Doctor } from '../../../core/models/doctor';
import { DoctorService } from '../../../core/services/doctor.service';

@Component({
  selector: 'app-doctor-table',
  templateUrl: './doctor-table.component.html',
  styleUrl: './doctor-table.component.css'
})
export class DoctorTableComponent {
  doctors: Doctor[] = [];
  addNewDoctorVisible: boolean = false;
  constructor(private doctorService: DoctorService){}
  ngOnInit(): void {
    this.getListDoctor();
  }
  getListDoctor(){
    this.doctorService.getListDoctor().subscribe(
      resp=>{
        console.log(resp)
        this.doctors = resp.data;
        console.log(this.doctors)
      }
    );
  }

}
