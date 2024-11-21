import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseChangePasswordComponent } from './nurse-change-password.component';

describe('NurseChangePasswordComponent', () => {
  let component: NurseChangePasswordComponent;
  let fixture: ComponentFixture<NurseChangePasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NurseChangePasswordComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NurseChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
