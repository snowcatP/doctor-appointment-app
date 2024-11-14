import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { MessageService } from 'primeng/api';
import { RegisterRequest } from '../../../../core/models/authentication.model';
import { AuthService } from '../../../../core/services/auth.service';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  formSignup: FormGroup;
  showPass: boolean = false;
  showConfirmPass: boolean = false;
  maxDate: Date;
  minDate: Date;
  errorMessage: string = '';
  @ViewChild('emailInput') emailInput: ElementRef;
  isLoading: boolean = false;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private route: Router
  ) {
    const currentYear = new Date().getFullYear();
    this.maxDate = new Date();
    this.minDate = new Date(currentYear - 150, 1, 1);
  }
  showPassword() {
    this.showPass = !this.showPass;
  }
  showConfirmPassword() {
    this.showConfirmPass = !this.showConfirmPass;
  }
  ngOnInit() {
    this.formSignup = this.fb.group({
      firstName: ['', [RxwebValidators.alpha(), RxwebValidators.required()]],
      lastName: ['', [RxwebValidators.alpha(), RxwebValidators.required()]],
      email: ['', [RxwebValidators.email(), RxwebValidators.required()]],
      password: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.password({
            validation: {
              maxLength: 15,
              minLength: 5,
              digit: true,
              specialCharacter: true,
            },
          }),
          RxwebValidators.minLength({ value: 5 }),
        ],
      ],
      confirmPassword: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.compare({ fieldName: 'password' }),
        ],
      ],
      phone: ['', [RxwebValidators.digit(), RxwebValidators.required()]],
      gender: ['0', [RxwebValidators.required()]],
    });
  }
  onSubmitRegister() {
    let registerData: RegisterRequest = {
      firstName: this.formSignup.controls['firstName'].value,
      lastName: this.formSignup.controls['lastName'].value,
      email: this.formSignup.controls['email'].value,
      phone: this.formSignup.controls['phone'].value,
      gender: this.formSignup.controls['gender'].value === 0 ? true : false,
      password: this.formSignup.controls['password'].value,
      confirmPassword: this.formSignup.controls['confirmPassword'].value,
    };

    this.isLoading = true;
    this.authService.onSignupPatient(registerData).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Signup successfully',
        });
        setTimeout(() => {
          this.route.navigateByUrl('/auth/login');
        }, 1500);
      },
      error: (err) => {
        this.errorMessage = err.error;
        this.isLoading = false;
        setTimeout(() => {
          this.emailInput.nativeElement.focus();
        }, 0);
      }
    });
  }
}
