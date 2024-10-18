import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './client/master-layout/register/register.component';
import { FooterComponent } from './component/footer/footer.component';
import { HeaderComponent } from './component/header/header.component';
import { ForgotPasswordComponent } from './client/master-layout/forgot-password/forgot-password.component';
import { HomeComponent } from './client/master-layout/home/home.component';
import { RouterLink, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CarouselModule } from 'primeng/carousel';
import { ToastModule } from 'primeng/toast';
import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { NbRoleProvider, NbSecurityModule } from '@nebular/security';
import { RoleProviderService } from './services/role-provider.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { LoginComponent } from './client/master-layout/login/login.component';
import { SearchDoctorComponent } from './client/master-layout/search-doctor/search-doctor.component';
import { SearchDoctorFilterComponent } from './client/master-layout/search-doctor/search-doctor-filter/search-doctor-filter.component';
import { SearchDoctorBreadCrumbComponent } from './client/master-layout/search-doctor/search-doctor-bread-crumb/search-doctor-bread-crumb.component';
import { DoctorProfileComponent } from './client/master-layout/doctor-profile/doctor-profile.component';
import { DoctorProfileBreadCrumbComponent } from './client/master-layout/doctor-profile/doctor-profile-bread-crumb/doctor-profile-bread-crumb.component';
import { PatientComponent } from './client/patient/patient.component';
import { PatientModule } from './client/patient/patient.module';
import { authInterceptor } from './auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    FooterComponent,
    HeaderComponent,
    ForgotPasswordComponent,
    HomeComponent,
    SearchDoctorComponent,
    SearchDoctorFilterComponent,
    SearchDoctorBreadCrumbComponent,
    DoctorProfileComponent,
    DoctorProfileBreadCrumbComponent,
    PatientComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    RouterLink,
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
    PatientModule
  ],
  providers: [
    { provide: NbRoleProvider, useClass: RoleProviderService },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    { provide: HTTP_INTERCEPTORS, useValue: authInterceptor, multi: true},
    provideHttpClient(),
    JwtHelperService,
    MessageService,
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
        DOCTOR:{
          parent: 'guest',
          create: '*',
          edit: '*',
          remove: '*',
          doctor: '*',

        }
      },
    }).providers,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
