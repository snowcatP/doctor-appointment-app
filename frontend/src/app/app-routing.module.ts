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
    path: 'chat',
    loadChildren: () =>
      import(
        './modules/chat/chat.module'
      ).then((m) => m.ChatModule),
    canActivate: [authGuard],
    data: {
      permission: ['PATIENT', 'DOCTOR']
    }
  },
  {
    path: 'patient',
    loadChildren: () =>
      import('./modules/patient/patient.module').then((m) => m.PatientModule),
    canActivate: [authGuard],
    data: {
      permission: ['PATIENT']
    }
  },
  {
    path: 'doctor',
    loadChildren: () =>
      import('./modules/doctor/doctor.module').then((m) => m.DoctorModule),
    canActivate: [authGuard],
    data: {
      permission: ['DOCTOR']
    }
  },
  {
    path: 'nurse',
    loadChildren: () =>
      import('./modules/nurse/nurse.module').then((m) => m.NurseModule),
    canActivate: [authGuard],
    data: {
      permission: ['NURSE']
    }
  },
  {
    path: 'chatbot',
    loadChildren: () =>
      import('./modules/chatbot/chatbot.module').then((m) => m.ChatbotModule),
    title: 'Chatbot',
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
