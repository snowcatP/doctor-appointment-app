import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorMypatientComponent } from './doctor-mypatient.component';

describe('DoctorMypatientComponent', () => {
  let component: DoctorMypatientComponent;
  let fixture: ComponentFixture<DoctorMypatientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorMypatientComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorMypatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
