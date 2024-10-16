import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './client/master-layout/register/register.component';
import { FooterComponent } from './component/footer/footer.component';
import { HeaderComponent } from './component/header/header.component';
import { ForgotPasswordComponent } from './client/master-layout/forgot-password/forgot-password.component';
import { HomeComponent } from './component/home/home.component';
import { RouterLink, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CarouselModule } from 'primeng/carousel';
import { ToastModule } from 'primeng/toast';
import { provideHttpClient } from '@angular/common/http';
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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    FooterComponent,
    HeaderComponent,
    ForgotPasswordComponent,
    HomeComponent,
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
  ],
  providers: [
    { provide: NbRoleProvider, useClass: RoleProviderService },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
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
