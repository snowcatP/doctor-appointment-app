import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingAppointmentSuccessComponent } from './booking-appointment-success.component';

describe('BookingAppointmentSuccessComponent', () => {
  let component: BookingAppointmentSuccessComponent;
  let fixture: ComponentFixture<BookingAppointmentSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookingAppointmentSuccessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingAppointmentSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
