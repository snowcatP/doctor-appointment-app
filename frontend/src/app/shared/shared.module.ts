import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScheduleComponent } from './components/schedule/schedule.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CalendarModule } from 'primeng/calendar';
import { CarouselModule } from 'primeng/carousel';
import { DividerModule } from 'primeng/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { BadgeModule } from 'primeng/badge';
import { PanelMenuModule } from 'primeng/panelmenu';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { Page404Component } from './components/page-404/page-404.component';
import { MatMenuModule } from '@angular/material/menu';
import { SidebarModule } from 'primeng/sidebar';
import { MatListModule } from '@angular/material/list';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
@NgModule({
  declarations: [
    ScheduleComponent,
    FooterComponent,
    HeaderComponent,
    Page404Component,
  ],
  imports: [
    CommonModule,
    ProgressSpinnerModule,
    CalendarModule,
    CarouselModule,
    DividerModule,
    MatTooltipModule,
    MatIconModule,
    FormsModule,
    MatButtonModule,
    BadgeModule,
    PanelMenuModule,
    MatMenuModule,
    SidebarModule,
    MatButtonModule,
    MatListModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    MatTooltipModule,
  ],
  exports: [
    ScheduleComponent,
    FooterComponent,
    HeaderComponent,
    Page404Component,
  ],
})
export class SharedModule {}
