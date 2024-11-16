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


@NgModule({
  declarations: [
    ScheduleComponent
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
  ],
  exports: [
    ScheduleComponent
  ]
})
export class SharedModule { }
