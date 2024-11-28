import { Component , ElementRef, ViewChild  } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { email, RxwebValidators } from '@rxweb/reactive-form-validators';
import { MessageService } from 'primeng/api';
import { UserChangePasswordRequest } from '../../../../core/models/authentication.model';
import { ApiResponse } from '../../../../core/models/patient.model';
import { AuthService } from '../../../../core/services/auth.service';
@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  formResetPassword: FormGroup;
  showPass: boolean = false;
  showConfirmPass: boolean = false;
  email: string;
  errorMessage: string = '';
  isLoading: boolean = false;
  token: string;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }
  showPassword() {
    this.showPass = !this.showPass;
  }
  showConfirmPassword() {
    this.showConfirmPass = !this.showConfirmPass;
  }
  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    this.formResetPassword = this.fb.group({
      newPassword: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.password({
            validation: {
              maxLength: 30,
              minLength: 8,
              digit: true,
            },
          }),
          RxwebValidators.minLength({ value: 8 }),
        ],
      ],
      confirmNewPassword: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.compare({ fieldName: 'newPassword' }),
        ],
      ],
    });
  }
  onSubmit() {

    this.isLoading = true;
    if (this.formResetPassword.invalid) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'warn',
        summary: 'Validation Error',
        detail: 'Please fill out all fields correctly.',
      });
      return;
    }

    const {newPassword, confirmNewPassword } = this.formResetPassword.value;
    console.log(newPassword, confirmNewPassword)

    if (!this.token) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'error',
        summary: 'Error',
        detail: 'Authentication token is missing!',
      });
      return;
    }

    // Gọi AuthService để reset password
    this.authService.resetPasswordNotLogin(this.token, newPassword, confirmNewPassword).subscribe(
      (response:ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: "Password reset successfully",
          });

          this.formResetPassword.reset(); // Reset form

          setTimeout(() => {
            this.router.navigate([`/auth/login`]);
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
