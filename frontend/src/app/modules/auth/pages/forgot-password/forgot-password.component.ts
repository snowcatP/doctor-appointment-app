import { Component, ElementRef, ViewChild  } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { email, error, RxwebValidators } from '@rxweb/reactive-form-validators';
import { MessageService } from 'primeng/api';
import { RegisterRequest } from '../../../../core/models/authentication.model';
import { AuthService } from '../../../../core/services/auth.service';
import { ApiResponse } from '../../../../core/models/patient.model';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  formForgot: FormGroup;
  email: string;
  errorMessage: string = '';
  isLoading: boolean = false;
  @ViewChild('emailInput') emailInput: ElementRef;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private route: Router
  ) {
  }

  ngOnInit() {
    this.formForgot = this.fb.group({
      email: ['', [RxwebValidators.email(), RxwebValidators.required()]]
    });
  }
  onSubmit() {

    this.email = this.formForgot.controls['email'].value;


    this.isLoading = true;
    this.authService.forgotPassword(this.email).subscribe(
      (response:ApiResponse) => {
        this.isLoading = false;
        console.log(response)
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: "Password reset request successfully",
          });

          this.formForgot.reset(); // Reset form

          setTimeout(() => {
            this.route.navigateByUrl('/auth/login');
          }, 1500);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message,
          });
        }
      },
      (err) => { 
        this.errorMessage = err.error;
        this.isLoading = false;
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: err.message,
        });
        setTimeout(() => {
          this.emailInput.nativeElement.focus();
        }, 0);
      }
      
    );
  }
}
