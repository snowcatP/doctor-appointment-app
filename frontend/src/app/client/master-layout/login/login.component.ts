import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  password,
  RxFormBuilder,
  RxReactiveFormsModule,
  RxwebValidators,
} from '@rxweb/reactive-form-validators';
import { AccountService } from '../../../services/account.service';
import { NbSidebarModule, NbLayoutModule, NbButtonModule, NbToastrModule } from '@nebular/theme';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RxReactiveFormsModule, FormsModule, NbButtonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  formLogin: FormGroup;
  showPass: boolean = false;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService
  ) { }
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
    });
  }
}
