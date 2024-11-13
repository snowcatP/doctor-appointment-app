import { Component, Input, OnInit } from '@angular/core';
import { User } from '../../../../core/models/authentication.model';

@Component({
  selector: 'app-doctor-sidebar',
  templateUrl: './doctor-sidebar.component.html',
  styleUrl: './doctor-sidebar.component.css'
})
export class DoctorSidebarComponent implements OnInit{
  @Input() doctorData: User;


  constructor() {}

  ngOnInit(): void {
    
  }
}
