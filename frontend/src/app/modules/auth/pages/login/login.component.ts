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
import { User } from '../../../../core/models/authentication.model';
import { Observable } from 'rxjs';
import * as fromAuth from '../../../../core/states/auth/auth.reducer';
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
  isLoading: boolean = false;
  user$: Observable<User>;
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
        this.isLoading = false;
        setTimeout(() => {
          this.emailInput.nativeElement.select();
        }, 0);
      }
    });

    this.action$
      .pipe(ofType(AuthActions.loginSuccess), takeUntil(this.unsubscribe$))
      .subscribe(() => {
        this.user$ = this.store.select(fromAuth.selectUser);
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Login successfully',
        });
        setTimeout(() => {
          this.user$.subscribe(user => {
            if(user?.role.roleName === "NURSE"){
              this.router.navigate(['/nurse/dashboard']);
            } else if (user?.role.roleName === "DOCTOR"){
              this.router.navigate(['/doctor']);
            }else{
              this.router.navigateByUrl('/');
            }
          })
          this.isLoading = false;
        }, 1000);
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
    this.isLoading = true;
    this.store.dispatch(AuthActions.loginRequest({ credential: loginRequest }));
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete(); // Cleanup subscription on component destroy
  }
}
