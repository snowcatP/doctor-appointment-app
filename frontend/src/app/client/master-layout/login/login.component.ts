import { CommonModule, NgIf } from '@angular/common';
import { Component, importProvidersFrom, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
} from '@angular/forms';
import {
  RxReactiveFormsModule,
  RxwebValidators,
} from '@rxweb/reactive-form-validators';

import { Router, RouterLink } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../../services/account.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  formLogin: FormGroup;
  showPass: boolean = false;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private messageService: MessageService,
    private route: Router
  ) {}
  ngOnInit(): void {
    this.formLogin = this.fb.group({
      username: ['', [RxwebValidators.required(), RxwebValidators.email()]],
      password: ['', [RxwebValidators.required()]],
    });
  }
  showPassword() {
    this.showPass = !this.showPass;
  }
  onLogin() {
    this.accountService.onLogin(this.formLogin.value).subscribe((res: any) => {
      localStorage.setItem('token', res.data.token);
      this.messageService.add({
        key: 'messageToast',
        severity: 'success',
        summary: 'Success',
        detail: 'Login successfully',
      });
      setTimeout(() => {
        this.route.navigateByUrl('/');
      }, 3500);
    });
  }
}
