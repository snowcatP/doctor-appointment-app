import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './core/guard/auth.guard';
import { Page404Component } from './core/components/page-404/page-404.component';
import { SearchDoctorComponent } from './modules/home/pages/search-doctor/search-doctor.component';
import { DoctorProfileComponent } from './modules/home/pages/doctor-profile/doctor-profile.component';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./modules/home/home.module')
    .then(m => m.HomeModule)
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
  {
    path: 'auth',
    loadChildren: () =>
      import
        ('./modules/auth/auth.module')
      .then((m) => m.AuthModule),
  },
  {
    path: 'booking',
    loadChildren: () =>
      import(
        './modules/home/pages/booking-appointment/booking-appointment.module'
      ).then((m) => m.BookingAppointmentModule),
  },
  {
    path: 'patient',
    loadChildren: () =>
      import('./modules/patient/patient.module').then((m) => m.PatientModule),
  },
  {
    path: 'doctor',
    loadChildren: () =>
      import('./modules/doctor/doctor.module').then((m) => m.DoctorModule),
  },
  {
    path: '**',
    component: Page404Component,
    title: 'Page 404 - Not Found',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
