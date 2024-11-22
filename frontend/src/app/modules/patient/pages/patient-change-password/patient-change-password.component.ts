import { Component } from '@angular/core';
import { FormsModule,FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { ApiResponse } from '../../../../core/models/patient.model';
@Component({
  selector: 'app-patient-change-password',
  templateUrl: './patient-change-password.component.html',
  styleUrl: './patient-change-password.component.css'
})
export class PatientChangePasswordComponent {
  formChangePasswordPatient: FormGroup;
  showPass: boolean = false;
  showConfirmPass: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router,
  ) {
    // Khởi tạo form
    this.formChangePasswordPatient = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmNewPassword: ['', [Validators.required]],
    });
  }

  showPassword() {
    this.showPass = !this.showPass;
  }

  showConfirmPassword() {
    this.showConfirmPass = !this.showConfirmPass;
  }

  onSubmit(): void {
    if (this.formChangePasswordPatient.invalid) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'warn',
        summary: 'Validation Error',
        detail: 'Please fill out all fields correctly.',
      });
      return;
    }

    const { newPassword, confirmNewPassword } = this.formChangePasswordPatient.value;

    const token = localStorage.getItem('token'); // Lấy token từ localStorage

    if (!token) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'error',
        summary: 'Error',
        detail: 'Authentication token is missing!',
      });
      return;
    }

    // Gọi AuthService để reset password
    this.authService.resetPassword(token, newPassword, confirmNewPassword).subscribe(
      (response:ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: "Change password successfully",
          });

          this.formChangePasswordPatient.reset(); // Reset form

          setTimeout(() => {
            this.router.navigate([`/patient/dashboard`]);
          }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message,
          });
        }
      },
      (error) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: error.message,
        });
      }
    );
  }
}
