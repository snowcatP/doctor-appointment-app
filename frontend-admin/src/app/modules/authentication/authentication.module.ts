import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthenticationRoutingModule } from './authentication-routing.module';
import { AuthenticationComponent } from './authentication.component';
import { RouterModule, RouterOutlet } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { MatButtonModule } from '@angular/material/button';
import { ToastModule } from 'primeng/toast';
import { RegisterComponent } from './register/register.component';
import { LogoutComponent } from './logout/logout.component';


@NgModule({
  declarations: [
    AuthenticationComponent,
    LoginComponent,
    RegisterComponent,
    LogoutComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    RouterOutlet,
    RouterModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    ToastModule,
  ]
})
export class AuthenticationModule { }
