import { Component } from '@angular/core';
import { NurseService } from '../../../../core/services/nurse.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../../../core/states/auth/auth.actions';
import * as fromAuth from '../../../../core/states/auth/auth.reducer';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-nurse-sidebar',
  templateUrl: './nurse-sidebar.component.html',
  styleUrls: ['./nurse-sidebar.component.css']
})
export class NurseSidebarComponent {
  nurseProfile: any = {};

  constructor(private nurseService: NurseService,
    private authService: AuthService,
    private store: Store<fromAuth.State>,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    // Subscribe to the profile observable to get live updates
    this.nurseService.userData$.subscribe(profile => {
      if (profile) {
        this.nurseProfile = profile;
      }
    });

    this.nurseService.getNurseProfile().subscribe(profile => {
        this.nurseProfile = profile.data;
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
