import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import {
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
import { provideAnimations } from '@angular/platform-browser/animations';
import { appInterceptorInterceptor } from './core/interceptors/authenticate.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { AuthModule } from './modules/auth/auth.module';
import { HomeModule } from './modules/home/home.module';
import { PatientModule } from './modules/patient/patient.module';
import { BookingAppointmentModule } from './modules/home/pages/booking-appointment/booking-appointment.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { authReducer } from './core/states/auth/auth.reducer';
import { AuthEffect } from './core/states/auth/auth.effects';
import { ScrollTopModule } from 'primeng/scrolltop';
import { SharedModule } from './shared/shared.module';
import { ChatbotModule } from './modules/chatbot/chatbot.module';
import { MatIconModule } from '@angular/material/icon';
import { MarkdownModule } from 'ngx-markdown';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    CommonModule,
    ToastModule,
    PatientModule,
    RouterModule,
    AuthModule,
    HttpClientModule,
    HomeModule,
    BookingAppointmentModule,
    SharedModule,
    ChatbotModule,
    ScrollTopModule,
    MatIconModule,
    StoreModule.forRoot({ auth: authReducer }, {}),
    EffectsModule.forRoot([AuthEffect]),
    MarkdownModule.forRoot(),
  ],
  providers: [
    { provide: NbRoleProvider, useClass: RoleProviderService },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    { provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher },
    provideHttpClient(withInterceptors([appInterceptorInterceptor])),
    JwtHelperService,
    MessageService,
    provideAnimations(),
    NbSecurityModule.forRoot({
      accessControl: {
        GUEST: {
          view: ['', 'booking'],
        },
        PATIENT: {
          parent: 'GUEST',
          view: ['', 'booking', 'patient'],
          create: '*',
          edit: '*',
          remove: '*',
        },
        DOCTOR: {
          parent: 'PATIENT',
          view: ['', 'doctor'],
          create: '*',
          edit: '*',
          remove: '*',
        },
        ADMIN: {
          parent: 'DOCTOR',
          view: '*',
          create: '*',
          edit: '*',
          remove: '*',
        },
      },
    }).providers,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
