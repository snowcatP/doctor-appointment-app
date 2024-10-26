import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CarouselModule } from 'primeng/carousel';
import { ToastModule } from 'primeng/toast';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  ErrorStateMatcher,
  MatNativeDateModule,
  ShowOnDirtyErrorStateMatcher,
} from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { NbRoleProvider, NbSecurityModule } from '@nebular/security';
import { RoleProviderService } from './services/role-provider.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { PatientModule } from './client/patient/patient.module';
import { FooterComponent } from './component/footer/footer.component';
import { HeaderComponent } from './component/header/header.component';
import { HomeComponent } from './client/master-layout/home/home.component';
import { SearchDoctorComponent } from './client/master-layout/search-doctor/search-doctor.component';
import { SearchDoctorFilterComponent } from './client/master-layout/search-doctor/search-doctor-filter/search-doctor-filter.component';
import { SearchDoctorBreadCrumbComponent } from './client/master-layout/search-doctor/search-doctor-bread-crumb/search-doctor-bread-crumb.component';
import { DoctorProfileComponent } from './client/master-layout/doctor-profile/doctor-profile.component';
import { DoctorProfileBreadCrumbComponent } from './client/master-layout/doctor-profile/doctor-profile-bread-crumb/doctor-profile-bread-crumb.component';
import { DoctorModule } from './client/doctor/doctor.module';
import { Page404Component } from './component/page-404/page-404.component';
import { BookingAppointmentModule } from './client/master-layout/booking-appointment/booking-appointment.module';
import { AuthenticationModule } from './client/authentication/authentication.module';
import { provideAnimations } from '@angular/platform-browser/animations';
import { httpErrorInterceptor } from './interceptors/http-error.interceptor';
import { appInterceptorInterceptor } from './interceptors/authenticate.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSelectModule} from '@angular/material/select';
import { HttpClientModule } from '@angular/common/http';
import {MatPaginatorModule} from '@angular/material/paginator';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    HomeComponent,
    SearchDoctorComponent,
    SearchDoctorFilterComponent,
    SearchDoctorBreadCrumbComponent,
    DoctorProfileComponent,
    DoctorProfileBreadCrumbComponent,
    Page404Component,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    CarouselModule,
    ToastModule,
    FormsModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatSelectModule,
    PatientModule,
    RouterModule,
    DoctorModule,
    BookingAppointmentModule,
    AuthenticationModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatSelectModule,
    HttpClientModule,
    MatPaginatorModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    PaginatorModule
  ],
  providers: [
    { provide: NbRoleProvider, useClass: RoleProviderService },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    { provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher },
    // provideHttpClient(withInterceptors([appInterceptorInterceptor, httpErrorInterceptor])),
    provideHttpClient(),
    JwtHelperService,
    MessageService,
    provideAnimations(),
    NbSecurityModule.forRoot({
      accessControl: {
        guest: {
          view: '*',
        },
        PATIENT: {
          parent: 'guest',
          create: '*',
          edit: '*',
          remove: '*',
        },
        DOCTOR: {
          parent: 'guest',
          create: '*',
          edit: '*',
          remove: '*',
          doctor: '*',
        },
      },
    }).providers,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
