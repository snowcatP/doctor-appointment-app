import { NgModule,CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NurseRoutingModule } from './nurse-routing.module';
import { NurseSidebarComponent } from './pages/nurse-sidebar/nurse-sidebar.component';
import { NurseDashboardComponent } from './pages/nurse-dashboard/nurse-dashboard.component';
import { NurseProfileComponent } from './pages/nurse-profile/nurse-profile.component';
import { NurseChangePasswordComponent } from './pages/nurse-change-password/nurse-change-password.component';
import { NurseBreadCrumbComponent } from './pages/nurse-bread-crumb/nurse-bread-crumb.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { ConfirmationService } from 'primeng/api';
import { NurseComponent } from './nurse.component';
import { DateFormatPipeModule } from '../../core/pipes/date-format.module';
import { ImageModule } from 'primeng/image';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CalendarModule } from 'primeng/calendar';
import { PaginatorModule } from 'primeng/paginator';
import { DialogModule } from 'primeng/dialog';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import { CreateMedicalRecordComponent } from './pages/create-medical-record/create-medical-record.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PasswordModule } from 'primeng/password';
import { DividerModule } from 'primeng/divider';
@NgModule({
  declarations: [
    NurseComponent,
    NurseSidebarComponent,
       NurseDashboardComponent,
       NurseProfileComponent,
       NurseChangePasswordComponent,
       NurseBreadCrumbComponent,
       CreateMedicalRecordComponent
  ],
  imports: [
    CommonModule,
    NurseRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    DateFormatPipeModule,
    ImageModule,
    ProgressSpinnerModule,
    CalendarModule,
    PaginatorModule,
    DialogModule,
    ScrollPanelModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    InputGroupModule,
    PasswordModule,
    InputGroupAddonModule,
    DividerModule
  ],
  providers: [ConfirmationService]
})
export class NurseModule { }
