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
import { RoleProviderService } from './core/services/role-provider.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { FooterComponent } from './core/components/footer/footer.component';
import { HeaderComponent } from './core/components/header/header.component';
import { Page404Component } from './core/components/page-404/page-404.component';
import { provideAnimations } from '@angular/platform-browser/animations';
import { httpErrorInterceptor } from './core/interceptors/http-error.interceptor';
import { appInterceptorInterceptor } from './core/interceptors/authenticate.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { HttpClientModule } from '@angular/common/http';
import { MatPaginatorModule } from '@angular/material/paginator';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { AuthModule } from './modules/auth/auth.module';
import { HomeModule } from './modules/home/home.module';
import { SearchDoctorBreadCrumbComponent } from './modules/home/pages/search-doctor/search-doctor-bread-crumb/search-doctor-bread-crumb.component';
import { SearchDoctorFilterComponent } from './modules/home/pages/search-doctor/search-doctor-filter/search-doctor-filter.component';
import { SearchDoctorComponent } from './modules/home/pages/search-doctor/search-doctor.component';
import { PaginatorModule } from 'primeng/paginator';
import { DoctorProfileBreadCrumbComponent } from './modules/home/pages/doctor-profile/doctor-profile-bread-crumb/doctor-profile-bread-crumb.component';
import { DoctorProfileComponent } from './modules/home/pages/doctor-profile/doctor-profile.component';
import { PatientModule } from './modules/patient/patient.module';
import { BookingAppointmentModule } from './modules/home/pages/booking-appointment/booking-appointment.module';
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    Page404Component,
    SearchDoctorBreadCrumbComponent,
    SearchDoctorFilterComponent,
    SearchDoctorComponent,
    DoctorProfileComponent,
    DoctorProfileBreadCrumbComponent,

    ///
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
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
    AuthModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatSelectModule,
    HttpClientModule,
    MatPaginatorModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    HomeModule,
    PaginatorModule,
    BookingAppointmentModule
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
