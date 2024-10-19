import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientBreadCrumbComponent } from './patient-bread-crumb.component';

describe('PatientBreadCrumbComponent', () => {
  let component: PatientBreadCrumbComponent;
  let fixture: ComponentFixture<PatientBreadCrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PatientBreadCrumbComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PatientBreadCrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
