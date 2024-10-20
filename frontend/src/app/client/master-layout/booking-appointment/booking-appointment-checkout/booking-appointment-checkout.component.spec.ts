import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingAppointmentCheckoutComponent } from './booking-appointment-checkout.component';

describe('BookingAppointmentCheckoutComponent', () => {
  let component: BookingAppointmentCheckoutComponent;
  let fixture: ComponentFixture<BookingAppointmentCheckoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookingAppointmentCheckoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingAppointmentCheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
