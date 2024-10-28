import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { MessageService } from 'primeng/api';
import {
  ErrorStateMatcher,
  ShowOnDirtyErrorStateMatcher,
} from '@angular/material/core';
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
import { HttpClientModule } from '@angular/common/http';
import { AuthModule } from './modules/auth/auth.module';
import { HomeModule } from './modules/home/home.module';
import { PatientModule } from './modules/patient/patient.module';
import { BookingAppointmentModule } from './modules/home/pages/booking-appointment/booking-appointment.module';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    Page404Component,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    ToastModule,
    PatientModule,
    RouterModule,
    AuthModule,
    BrowserAnimationsModule,
    HttpClientModule,
    HomeModule,
    BookingAppointmentModule,
    MatMenuModule,
    MatIconModule,
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
