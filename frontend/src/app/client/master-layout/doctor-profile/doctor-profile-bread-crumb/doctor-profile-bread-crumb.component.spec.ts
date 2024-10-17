import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorProfileBreadCrumbComponent } from './doctor-profile-bread-crumb.component';

describe('DoctorProfileBreadCrumbComponent', () => {
  let component: DoctorProfileBreadCrumbComponent;
  let fixture: ComponentFixture<DoctorProfileBreadCrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorProfileBreadCrumbComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorProfileBreadCrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
