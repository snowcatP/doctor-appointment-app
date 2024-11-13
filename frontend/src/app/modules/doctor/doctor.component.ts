import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../core/states/auth/auth.reducer';
import { Observable } from 'rxjs';
import { User } from '../../core/models/authentication.model';
@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrl: './doctor.component.css',
})
export class DoctorComponent implements OnInit {
  doctorData: User;

  constructor(private store: Store<fromAuth.State>) {}

  ngOnInit(): void {
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
