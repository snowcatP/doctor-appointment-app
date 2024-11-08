import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorReviewComponent } from './doctor-review.component';

describe('DoctorReviewComponent', () => {
  let component: DoctorReviewComponent;
  let fixture: ComponentFixture<DoctorReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorReviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
