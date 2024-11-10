import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../states/auth/auth.reducer';
import { map, Observable, of } from 'rxjs';
import { User } from '../../models/authentication.model';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import * as AuthActions from '../../states/auth/auth.actions';
import { MessageService } from 'primeng/api';
import { MediaMatcher } from '@angular/cdk/layout';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  token$: Observable<string>;
  user$: Observable<User>;
  sidebarVisible: boolean = false;
  constructor(
    public accessChecker: NbAccessChecker,
    private store: Store<fromAuth.State>,
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.token$ = this.store.select(fromAuth.selectToken);
    this.user$ = this.store.select(fromAuth.selectUser);
  }

  goToDashboard() {
    this.user$.subscribe(user => {
      if (user?.role.roleName === 'PATIENT') {
        this.router.navigate(['/patient']);
      } else if (user?.role.roleName === 'DOCTOR') {
        this.router.navigate(['/doctor']);
      } else {
        console.warn('Unknown role, unable to redirect');
      }
    });
  }

  goToProfile() {
    this.user$.subscribe(user => {
      if (user?.role.roleName === 'PATIENT') {
        this.router.navigate(['/patient/profile']);
      } else if (user?.role.roleName === 'DOCTOR') {
        this.router.navigate(['/doctor/profile']);
      } else {
        console.warn('Unknown role, unable to redirect');
      }
    });
  }

  logout() {
    this.authService.logout().subscribe({
      next: (res) => {
        this.store.dispatch(
          AuthActions.logout({ logoutSuccess: 'Logout Success' })
        );
      },
      error: (err) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Fail to logout',
        });
      },
    });
  }
}
