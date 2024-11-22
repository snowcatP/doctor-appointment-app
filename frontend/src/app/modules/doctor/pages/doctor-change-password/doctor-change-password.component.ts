import { Component,signal } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { ApiResponse } from '../../../../core/models/doctor.model';
@Component({
  selector: 'app-doctor-change-password',
  templateUrl: './doctor-change-password.component.html',
  styleUrl: './doctor-change-password.component.css'
})
export class DoctorChangePasswordComponent {
  formChangePassword: FormGroup;
  showPass: boolean = false;
  showConfirmPass: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router,
  ) {
    // Khởi tạo form
    this.formChangePassword = this.fb.group({
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
    if (this.formChangePassword.invalid) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'warn',
        summary: 'Validation Error',
        detail: 'Please fill out all fields correctly.',
      });
      return;
    }

    const { newPassword, confirmNewPassword } = this.formChangePassword.value;

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

          this.formChangePassword.reset(); // Reset form

          setTimeout(() => {
            this.router.navigate([`/doctor`]);
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
