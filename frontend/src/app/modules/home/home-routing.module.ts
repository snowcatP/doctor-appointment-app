import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchDoctorComponent } from './pages/search-doctor/search-doctor.component';
import { DoctorProfileComponent } from './pages/doctor-profile/doctor-profile.component';
import { HomeIndexComponent } from './pages/home-index/home-index.component';

const routes: Routes = [
  {
    path: '',
    component: HomeIndexComponent,
    title: 'Home',
  },
  {
    path: 'search-doctor',
    component: SearchDoctorComponent,
    title: 'Search Doctor',
  },
  {
    path: 'doctor-profile/:id',
    component: DoctorProfileComponent,
    title: 'Doctor Profile',
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
