import { Component } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../../../core/states/auth/auth.reducer';
import * as AuthActions from '../../../../core/states/auth/auth.actions';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-doctor-sidebar',
  templateUrl: './doctor-sidebar.component.html',
  styleUrl: './doctor-sidebar.component.css'
})
export class DoctorSidebarComponent {
  doctorProfile: any = {};

  constructor(private doctorService: DoctorService,
    private authService: AuthService,
    private store: Store<fromAuth.State>,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    // Subscribe to the profile observable to get live updates
    this.doctorService.userData$.subscribe(profile => {
      if (profile) {
        this.doctorProfile = profile;
      }
    });

    this.doctorService.getDoctorProfile().subscribe(profile => {
        this.doctorProfile = profile.data;
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
