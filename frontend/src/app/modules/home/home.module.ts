import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { RouterModule } from '@angular/router';
import { CarouselModule } from 'primeng/carousel';
import { SearchDoctorComponent } from './pages/search-doctor/search-doctor.component';
import { SearchDoctorFilterComponent } from './pages/search-doctor/search-doctor-filter/search-doctor-filter.component';
import { SearchDoctorBreadCrumbComponent } from './pages/search-doctor/search-doctor-bread-crumb/search-doctor-bread-crumb.component';
import { DropdownModule } from 'primeng/dropdown';
import { PaginatorModule } from 'primeng/paginator';
import { DoctorProfileComponent } from './pages/doctor-profile/doctor-profile.component';
import { DoctorProfileBreadCrumbComponent } from './pages/doctor-profile/doctor-profile-bread-crumb/doctor-profile-bread-crumb.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home.component';
import { HomeIndexComponent } from './pages/home-index/home-index.component';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';
@NgModule({
  declarations: [
    HomeComponent,
    HomeIndexComponent,
    SearchDoctorComponent,
    SearchDoctorBreadCrumbComponent,
    SearchDoctorFilterComponent,
    DoctorProfileComponent,
    DoctorProfileBreadCrumbComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    RouterModule,
    CarouselModule,
    DropdownModule,
    PaginatorModule,
    FormsModule,
    ReactiveFormsModule,
    ImageModule,
    DialogModule
  ]
})
export class HomeModule { }
