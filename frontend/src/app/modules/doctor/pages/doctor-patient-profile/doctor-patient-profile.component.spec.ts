import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorPatientProfileComponent } from './doctor-patient-profile.component';

describe('DoctorPatientProfileComponent', () => {
  let component: DoctorPatientProfileComponent;
  let fixture: ComponentFixture<DoctorPatientProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorPatientProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorPatientProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
