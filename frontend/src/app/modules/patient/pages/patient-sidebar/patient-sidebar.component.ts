import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../../../core/states/auth/auth.actions';
import * as fromAuth from '../../../../core/states/auth/auth.reducer';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-patient-sidebar',
  templateUrl: './patient-sidebar.component.html',
  styleUrl: './patient-sidebar.component.css'
})
export class PatientSidebarComponent implements OnInit {
  patientProfile: any = {};

  constructor(private authService: AuthService,
    private store: Store<fromAuth.State>,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.authService.getUserData().subscribe(profile => {
        this.patientProfile = profile;
      
    });

    this.authService.userData$.subscribe(profile => {
      if (profile) {
        this.patientProfile = profile;
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
