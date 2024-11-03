import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DoctorRoutingModule } from './doctor-routing.module';
import { DoctorComponent } from './doctor.component';
import { DoctorTableComponent } from './doctor-table/doctor-table.component';
import { RouterOutlet } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { FileUploadModule } from 'primeng/fileupload';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { DialogModule } from 'primeng/dialog';
@NgModule({
  declarations: [
    DoctorComponent,
    DoctorTableComponent
  ],
  imports: [
    CommonModule,
    DoctorRoutingModule,
    RouterOutlet,
    TableModule, 
    ToolbarModule, 
    ButtonModule, 
    FileUploadModule, 
    CommonModule,
    HttpClientModule,
    DialogModule,

  ]
})
export class DoctorModule { }
