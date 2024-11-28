import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidationErrors, AbstractControl } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { ApiResponse } from '../../../../core/models/doctor.model';
@Component({
  selector: 'app-nurse-change-password',
  templateUrl: './nurse-change-password.component.html',
  styleUrl: './nurse-change-password.component.css'
})
export class NurseChangePasswordComponent {
  formChangePassword: FormGroup;
  showPass: boolean = false;
  showConfirmPass: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router,
  ) {
    this.formChangePassword = this.fb.group({
      oldPassword: ['', [Validators.required, Validators.minLength(8)]],
      newPassword: ['', [Validators.required, this.passwordValidator]],
      confirmNewPassword: ['', [Validators.required]],
    }, { validators: this.passwordsMatchValidator });
  }

  // Custom validator for password
  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasNumber = /\d/.test(value);
    const isValidLength = value.length >= 8;

    if (!hasNumber || !isValidLength) {
      return { passwordInvalid: true };
    }
    return null;
  }

  // Validator to check if passwords match
  passwordsMatchValidator(group: FormGroup): ValidationErrors | null {
    const newPassword = group.get('newPassword')?.value;
    const confirmNewPassword = group.get('confirmNewPassword')?.value;
    return newPassword === confirmNewPassword ? null : { passwordsMismatch: true };
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

    const {oldPassword, newPassword, confirmNewPassword } = this.formChangePassword.value;

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
    this.authService.resetPassword(token, oldPassword, newPassword, confirmNewPassword).subscribe(
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
            this.router.navigate([`/nurse/dashboard`]);
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
