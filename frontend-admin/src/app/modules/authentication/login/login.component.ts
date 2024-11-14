import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../../core/services/account.service';
import { MessageService } from 'primeng/api';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginRequest } from '../../../core/models/authentication';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private router: Router, private accountService: AccountService, private messageService: MessageService) { }
  hide = true;
  form = new FormGroup({
    uname: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  get f() {
    return this.form.controls;
  }

  submit() {
    let loginRequest: LoginRequest = {
      email: this.form.controls['uname'].value!,
      password: this.form.controls['password'].value!
    };
    this.accountService.onLogin(loginRequest).subscribe({
      next: (res:any) => {
        localStorage.setItem('token', res.token);
        this.router.navigateByUrl('')
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Login successfully',
        });
        
      }
    })
    // console.log(this.form.value);
    
  }
}
