import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { RxwebValidators } from '@rxweb/reactive-form-validators';

import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoginRequest } from '../../../../core/models/authentication.model';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../../../core/states/auth/auth.actions';
import { selectErrorMessage } from '../../../../core/states/auth/auth.reducer';
import { Actions, ofType } from '@ngrx/effects';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  formLogin: FormGroup;
  showPass: boolean = false;
  errorMessage: string = '';
  @ViewChild('email') emailInput: ElementRef;
  private unsubscribe$ = new Subject<void>();
  constructor(
    private fb: FormBuilder,
    private store: Store,
    private action$: Actions,
    private router: Router,
    private messageService: MessageService
  ) {}
  ngOnInit(): void {
    this.formLogin = this.fb.group({
      email: ['', [RxwebValidators.required(), RxwebValidators.email()]],
      password: ['', [RxwebValidators.required()]],
    });

    this.store.select(selectErrorMessage).subscribe((error) => {
      if (error) {
        this.errorMessage = 'Wrong email or password.';
        setTimeout(() => {
          this.emailInput.nativeElement.select();
        }, 0);
      }
    });

    this.action$
      .pipe(ofType(AuthActions.loginSuccess), takeUntil(this.unsubscribe$))
      .subscribe(() => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Login successfully',
        });
        setTimeout(() => {
          this.router.navigateByUrl('/');
        }, 1500);
      });
  }
  showPassword() {
    this.showPass = !this.showPass;
  }
  onLogin() {
    let loginRequest: LoginRequest = {
      email: this.formLogin.controls['email'].value,
      password: this.formLogin.controls['password'].value,
    };
    this.store.dispatch(AuthActions.loginRequest({ credential: loginRequest }));
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete(); // Cleanup subscription on component destroy
  }
}
