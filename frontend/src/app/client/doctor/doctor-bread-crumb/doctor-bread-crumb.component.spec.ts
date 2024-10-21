import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorBreadCrumbComponent } from './doctor-bread-crumb.component';

describe('DoctorBreadCrumbComponent', () => {
  let component: DoctorBreadCrumbComponent;
  let fixture: ComponentFixture<DoctorBreadCrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorBreadCrumbComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorBreadCrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
