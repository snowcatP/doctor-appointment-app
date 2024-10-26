import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../../services/account.service';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  formSignup: FormGroup;
  showPass: boolean = false;
  showRepeatPass: boolean = false;
  maxDate: Date;
  minDate: Date;
  genders: string[] = ['Male', 'Female'];
  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
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
  showRepeatPassword() {
    this.showRepeatPass = !this.showRepeatPass;
  }
  ngOnInit() {
    this.formSignup = this.fb.group({
      firstName: ['', [RxwebValidators.alpha(), RxwebValidators.required()]],
      lastName: ['', [RxwebValidators.alpha(), RxwebValidators.required()]],
      email: ['', [RxwebValidators.email(), , RxwebValidators.required()]],
      password: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.password({
            validation: {
              maxLength: 10,
              minLength: 5,
              digit: true,
              specialCharacter: true,
            },
            message: {
              minLength: 'Minimum Character length should be 5.',
              maxLength: 'MaxLength allowed is 10',
              digit:
                'Password should contains digit and special charaters(Ex: @, #, &,...)',
              specialCharacter:
                'Password should contains digit and special charaters(Ex: @, #, &,...)',
            },
          }),
        ],
      ],
      repeatPassword: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.password({
            validation: {
              maxLength: 10,
              minLength: 5,
              digit: true,
              specialCharacter: true,
            },
            message: {
              minLength: 'Minimum Character length should be 5.',
              maxLength: 'MaxLength allowed is 10',
              digit:
                'Password should contains digit and special charaters(Ex: @, #, &,...)',
              specialCharacter:
                'Password should contains digit and special charaters(Ex: @, #, &,...)',
            },
          }),
        ],
      ],
      phone: ['', [RxwebValidators.alphaNumeric()]],
      gender: ['', [RxwebValidators.required()]],
    });
  }
  onClickSignUpPatient(data: any) {
    this.accountService.onSignupPatient(data).subscribe((res: any) => {
      this.messageService.add({
        key: 'messageToast',
        severity: 'success',
        summary: 'Success',
        detail: 'Signup successfully',
      });
      setTimeout(() => {
        this.route.navigateByUrl('/auth/login');
      }, 3500);
    });
  }
}
