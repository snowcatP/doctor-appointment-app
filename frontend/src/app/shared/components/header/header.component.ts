import {
  ChangeDetectorRef,
  Component,
  inject,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../../core/states/auth/auth.reducer';
import { map, Observable, of } from 'rxjs';
import { User } from '../../../core/models/authentication.model';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import * as AuthActions from '../../../core/states/auth/auth.actions';
import { MessageService } from 'primeng/api';
import { ReferenceCodeRequest } from '../../../core/models/appointment.model';
import { AppointmentService } from '../../../core/services/appointment.service';
import SockJS from 'sockjs-client';
import { host } from '../../../../environments/environment';
import * as Stomp from 'stompjs';
import { jwtDecode } from 'jwt-decode';
import { AppNotification } from '../../../core/models/notification.model';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  token$: Observable<string>;
  user$: Observable<User>;
  role$: Observable<string>;
  sidebarVisible: boolean = false;
  referenceCode: string;
  referenceCodeRequest: ReferenceCodeRequest;
  appointment!: any;
  visible: boolean = false;
  socketClient: any = null;
  private notificationSubscription: any;
  constructor(
    public accessChecker: NbAccessChecker,
    private store: Store<fromAuth.State>,
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService,
    private appointmentService: AppointmentService
  ) {}

  ngOnInit(): void {
    this.token$ = this.store.select(fromAuth.selectToken);
    this.user$ = this.store.select(fromAuth.selectUser);
    this.role$ = this.store.select(fromAuth.selectRole);
    if (localStorage.getItem('token') != null) {
      let ws = new SockJS(`${host}/ws`);
      this.socketClient = Stomp.over(ws);
      let token = localStorage.getItem('token');
      const decodedToken: any = jwtDecode(token);
      const userId = decodedToken.email;
      console.log(userId);
      this.socketClient.connect({ 'Authorization:': 'Bearer ' + token }, () => {
        console.log('Connecting to WS ...');
        this.notificationSubscription = this.socketClient.subscribe(
          `/user/${userId}/notification`,
          (message: any) => {
            console.log('Receiving message', message);
            debugger;
            const notification: AppNotification = JSON.parse(message.body);
            if (notification) {
              switch (notification.status) {
                case 'PENDING':
                  this.messageService.add({
                    key: 'messageToast',
                    severity: 'success',
                    summary: `AppointmentId: ${notification.appointmentId}`,
                    detail: `${notification.message}`,
                  });
                  break;
                case 'ACCEPT':
                  this.messageService.add({
                    key: 'messageToast',
                    severity: 'success',
                    summary: `AppointmentId: ${notification.appointmentId}`,
                    detail: `${notification.message}`,
                  });
                  break;
                case 'IN_PROGRESS':
                  this.messageService.add({
                    key: 'messageToast',
                    severity: 'info',
                    summary: `AppointmentId: ${notification.appointmentId}`,
                    detail: `${notification.message}`,
                  });
                  break;
                case 'RESCHEDULED':
                  this.messageService.add({
                    key: 'messageToast',
                    severity: 'warn',
                    summary: `AppointmentId: ${notification.appointmentId}`,
                    detail: `${notification.message}`,
                  });
                  break;
                case 'CANCELLED':
                  this.messageService.add({
                    key: 'messageToast',
                    severity: 'error',
                    summary: `AppointmentId: ${notification.appointmentId}`,
                    detail: `${notification.message}`,
                  });
                  break;
              }
            }
          }
        );
      });
    }
  }

  goToHome() {
    this.user$.subscribe((user) => {
      if (user?.role.roleName === 'PATIENT') {
        this.router.navigate(['/']);
      } else if (user?.role.roleName === 'DOCTOR') {
        this.router.navigate(['/doctor']);
      } else if (user?.role.roleName === 'NURSE') {
        this.router.navigate(['/nurse']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }

  goToDashboard() {
    this.user$.subscribe((user) => {
      if (user?.role.roleName === 'PATIENT') {
        this.router.navigate(['/patient']);
      } else if (user?.role.roleName === 'DOCTOR') {
        this.router.navigate(['/doctor']);
      } else if (user?.role.roleName === 'NURSE') {
        this.router.navigate(['/nurse']);
      } else {
        console.warn('Unknown role, unable to redirect');
      }
    });
  }

  goToProfile() {
    this.user$.subscribe((user) => {
      if (user?.role.roleName === 'PATIENT') {
        this.router.navigate(['/patient/profile']);
      } else if (user?.role.roleName === 'DOCTOR') {
        this.router.navigate(['/doctor/profile']);
      } else if (user?.role.roleName === 'NURSE') {
        this.router.navigate(['/nurse/profile']);
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

  closeSideBar() {
    this.sidebarVisible = false;
  }

  fetchGetAppointmentByReferenceCode(): void {
    if (!this.referenceCode) {
      this.messageService.add({
        key: 'messageToast',
        severity: 'warn',
        summary: 'Warning',
        detail: 'Please enter a appointment code.',
      });
      return;
    }
    this.referenceCodeRequest = { referenceCode: this.referenceCode };
    this.appointmentService
      .getAppointmentByReferenceCode(this.referenceCodeRequest)
      .subscribe(
        (response) => {
          if (response.statusCode === 200) {
            this.appointment = response.data;
            this.appointmentService.showDialog(this.appointment);
            this.messageService.add({
              key: 'messageToast',
              severity: 'success',
              summary: 'Success',
              detail: 'Appointment Successfully Found',
            });
          } else {
            this.messageService.add({
              key: 'messageToast',
              severity: 'error',
              summary: 'Failed',
              detail: response.message,
            });
          }
        },
        (error) => {
          console.error('Error fetching appointment', error);
        }
      );
  }
}
