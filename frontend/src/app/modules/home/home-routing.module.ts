import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchDoctorComponent } from './pages/search-doctor/search-doctor.component';
import { HomeComponent } from './home.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'Home',
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
