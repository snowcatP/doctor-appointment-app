import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { Router, RouterLink } from '@angular/router';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { AccountService } from '../../services/account.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatNativeDateModule,
    MatInputModule,
    MatRadioModule,
    CommonModule,
    RouterLink,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent implements OnInit {
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
      address: ['', [RxwebValidators.alphaNumeric({ allowWhiteSpace: true })]],
      dateOfBirth: ['', [RxwebValidators.date(), RxwebValidators.required()]],
      gender: ['', [RxwebValidators.required()]],
    });
  }
  onClickSignUpPatient(data: any) {
    this.accountService.onSignupPatient(data).subscribe((res: any) => {
      console.log(res);

      this.messageService.add({
        key: 'messageToast',
        severity: 'success',
        summary: 'Success',
        detail: 'Signup successfully',
      });
      setTimeout(() => {this.route.navigateByUrl('/login')}, 3500);
    });
  }
}
