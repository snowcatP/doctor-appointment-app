import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorAddNewComponent } from './doctor-add-new.component';

describe('DoctorAddNewComponent', () => {
  let component: DoctorAddNewComponent;
  let fixture: ComponentFixture<DoctorAddNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoctorAddNewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorAddNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
