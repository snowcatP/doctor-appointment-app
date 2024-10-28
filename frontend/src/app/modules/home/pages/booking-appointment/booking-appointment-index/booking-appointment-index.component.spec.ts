import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingAppointmentIndexComponent } from './booking-appointment-index.component';

describe('BookingAppointmentIndexComponent', () => {
  let component: BookingAppointmentIndexComponent;
  let fixture: ComponentFixture<BookingAppointmentIndexComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookingAppointmentIndexComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingAppointmentIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
