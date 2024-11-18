import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './core/guard/auth.guard';
import { Page404Component } from './shared/components/page-404/page-404.component';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./modules/home/home.module')
    .then(m => m.HomeModule)
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
    canActivate: [authGuard],
    data: {
      permission: ['PATIENT'],
      redirectTo: '/error-404'
    }
  },
  {
    path: 'doctor',
    loadChildren: () =>
      import('./modules/doctor/doctor.module').then((m) => m.DoctorModule),
    canActivate: [authGuard],
    data: {
      permission: ['DOCTOR'],
      redirectTo: '/error-404'
    }
  },
  {
    path: 'error404',
    component: Page404Component,
    title: 'Page 404 - Not Found',
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
