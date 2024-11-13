import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../../core/models/authentication.model';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../core/states/auth/auth.reducer';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent implements OnInit {
  showSidebar: boolean = true;
  doctorData: User;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private store: Store<fromAuth.State>) {}

  ngOnInit(): void {
    this.router.events.subscribe(() => {
      // Hide the sidebar if the current route is /doctor/patient-profile
      this.showSidebar = !this.router.url.includes('/doctor/patient-profile');
    });
    this.store.select(fromAuth.selectUser).subscribe(
      (res) => {
        this.doctorData = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
