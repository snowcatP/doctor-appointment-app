import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DoctorRoutingModule } from './doctor-routing.module';
import { DoctorComponent } from './doctor.component';
import { DoctorTableComponent } from './doctor-table/doctor-table.component';
import { RouterOutlet } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { DialogModule } from 'primeng/dialog';
import {  FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputMaskModule } from 'primeng/inputmask';
import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
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
    ToastModule,
    ToolbarModule, 
    ButtonModule, 
    FileUploadModule, 
    DialogModule,
    ReactiveFormsModule,
    FormsModule,
    InputGroupAddonModule,
    InputGroupModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    RadioButtonModule,
    ReactiveFormsModule, 
    InputMaskModule,
    FileUploadModule,
    CalendarModule,
    DropdownModule
    
  ]
})
export class DoctorModule { }
