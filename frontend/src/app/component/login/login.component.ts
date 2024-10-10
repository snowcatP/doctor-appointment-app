import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { RxReactiveFormsModule,RxwebValidators } from '@rxweb/reactive-form-validators';
import { AccountService } from '../../services/account.service';
import { Router, RouterLink } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { CarouselModule } from 'primeng/carousel';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RxReactiveFormsModule, FormsModule, RouterLink, ToastModule, CarouselModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers:[MessageService]
})
export class LoginComponent implements OnInit {
  formLogin: FormGroup;
  showPass: boolean = false;

  constructor(private fb: FormBuilder, private accountService: AccountService, private messageService: MessageService, private route: Router,
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
      this.messageService.add({
        key: 'messageToast',
        severity: 'success',
        summary: 'Success',
        detail: 'Login successfully',
      });
      setTimeout(() => {this.route.navigateByUrl('/')}, 3500);
    });
  }
}
