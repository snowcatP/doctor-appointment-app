import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { PasswordModule } from 'primeng/password';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { RatingModule } from 'primeng/rating';
import { SkeletonModule } from 'primeng/skeleton';
import { ImageModule } from 'primeng/image';
import { ProgressBarModule } from 'primeng/progressbar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ChipsModule } from 'primeng/chips';
import { MultiSelectModule } from 'primeng/multiselect';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { PatientTableComponent } from './patient-table/patient-table.component';
import { PatientComponent } from './patient.component';
import { PatientRoutingModule } from './patient-routing.module';
import { TagModule } from 'primeng/tag';
import { PatientEditComponent } from './patient-edit/patient-edit.component';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';

@NgModule({
  declarations: [PatientComponent, PatientTableComponent, PatientEditComponent, PatientDashboardComponent],
  imports: [
    CommonModule,
    PatientRoutingModule,
    ConfirmDialogModule,
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
    DropdownModule,
    PasswordModule,
    RatingModule,
    SkeletonModule,
    ImageModule,
    ProgressBarModule,
    ProgressSpinnerModule,
    ChipsModule,
    MultiSelectModule,
    AvatarModule,
    AvatarGroupModule,
    InputTextModule,
    TagModule,
  ],
})
export class PatientModule {}
