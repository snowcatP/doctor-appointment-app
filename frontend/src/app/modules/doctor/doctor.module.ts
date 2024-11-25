import { ScrollPanelModule } from 'primeng/scrollpanel';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DoctorRoutingModule } from './doctor-routing.module';
import { DoctorAppointmentComponent } from './pages/doctor-appointment/doctor-appointment.component';
import { DoctorBreadCrumbComponent } from './pages/doctor-bread-crumb/doctor-bread-crumb.component';
import { DoctorCalendarComponent } from './pages/doctor-calendar/doctor-calendar.component';
import { DoctorDashboardComponent } from './pages/doctor-dashboard/doctor-dashboard.component';
import { DoctorProfileComponent } from './pages/doctor-profile/doctor-profile.component';
import { DoctorScheduleComponent } from './pages/doctor-schedule/doctor-schedule.component';
import { DoctorSidebarComponent } from './pages/doctor-sidebar/doctor-sidebar.component';
import { DoctorComponent } from './doctor.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { DoctorReviewComponent } from './pages/doctor-review/doctor-review.component';
import { ReplyDialogComponent } from './pages/doctor-review/reply-dialog/reply-dialog.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { PaginatorModule } from 'primeng/paginator';
import { DoctorMypatientComponent } from './pages/doctor-mypatient/doctor-mypatient.component';
import { ImageModule } from 'primeng/image';
import { CalendarModule } from 'primeng/calendar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DateFormatPipeModule } from '../../core/pipes/date-format.module';
import { DoctorPatientProfileComponent } from './pages/doctor-patient-profile/doctor-patient-profile.component';
import { MatTabsModule } from '@angular/material/tabs';
import { DialogModule } from 'primeng/dialog';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { CarouselModule } from 'primeng/carousel';
import { DividerModule } from 'primeng/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ConfirmationService } from 'primeng/api';
import { SharedModule } from '../../shared/shared.module';
import { DropdownModule } from 'primeng/dropdown';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DoctorChangePasswordComponent } from './pages/doctor-change-password/doctor-change-password.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import { PasswordModule } from 'primeng/password';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import { InputTextareaModule } from 'primeng/inputtextarea';
@NgModule({
  declarations: [
    DoctorAppointmentComponent,
    DoctorBreadCrumbComponent,
    DoctorCalendarComponent,
    DoctorDashboardComponent,
    DoctorProfileComponent,
    DoctorScheduleComponent,
    DoctorSidebarComponent,
    DoctorComponent,
    DoctorReviewComponent,
    ReplyDialogComponent,
    DoctorMypatientComponent,
    DoctorPatientProfileComponent,
    DoctorChangePasswordComponent,
  ],
  imports: [
    CommonModule,
    DoctorRoutingModule,
    FullCalendarModule,
    FormsModule,
    MatDialogModule,
    PaginatorModule,
    ImageModule,
    CalendarModule,
    ProgressSpinnerModule,
    DateFormatPipeModule,
    MatTabsModule,
    DialogModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    CarouselModule,
    DividerModule,
    MatTooltipModule,
    MatIconModule,
    ScrollPanelModule,
    MatButtonModule,
    ConfirmPopupModule,
    SharedModule,
    DropdownModule,
    InputGroupModule,
    InputGroupAddonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    PasswordModule,
    MatSelectModule,
    MatInputModule,
    InputTextareaModule
  ],
  providers: [ConfirmationService],
})
export class DoctorModule { }
