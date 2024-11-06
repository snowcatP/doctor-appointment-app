import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { RxwebValidators } from '@rxweb/reactive-form-validators';

import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../../../core/services/account.service';
import { LoginRequest } from '../../../../core/models/authentication.model';
import { AuthService } from '../../../../core/services/auth.service';
import { PatientService } from '../../../../core/services/patient.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  formLogin: FormGroup;
  showPass: boolean = false;
  errorMessage: string = "";
  isLoggedIn: boolean = false;
  patientProfile: any; // Variable to store patient profile data
  @ViewChild('email') emailInput: ElementRef;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private messageService: MessageService,
    private route: Router,
    private authService: AuthService,
    private patientService: PatientService
  ) {}
  ngOnInit(): void {
    this.formLogin = this.fb.group({
      email: ['', [RxwebValidators.required(), RxwebValidators.email()]],
      password: ['', [RxwebValidators.required()]],
    });
  }
  showPassword() {
    this.showPass = !this.showPass;
  }
  onLogin() {
    let loginRequest: LoginRequest = {
      email: this.formLogin.controls['email'].value,
      password: this.formLogin.controls['password'].value
    };

    this.accountService.onLogin(loginRequest).subscribe({
      next: (res: any) => {
      localStorage.setItem('token', res.data.token);
      this.authService.updateLoginStatus(true); // Update login status
      this.messageService.add({
        key: 'messageToast',
        severity: 'success',
        summary: 'Success',
        detail: 'Login successfully',
      });
      this.fetchPatientProfile();
      setTimeout(() => {
        this.route.navigateByUrl('/');
      }, 1500);
    },
    error: () => {
      this.errorMessage = "Wrong email or password."
      setTimeout(() => {
        this.emailInput.nativeElement.focus();
      }, 0);
    }
    });
  }

  checkError(control: FormControl) {
    
  }

  fetchPatientProfile(): void {
    this.patientService.fetchMyInfo().subscribe(
      (response) => {
        if (response != null) {
          this.patientService.setPatientProfile(response);
        }
      },
      (error) => {
        console.error('Error fetching patient profile:', error);
      }
    );
  }
}
