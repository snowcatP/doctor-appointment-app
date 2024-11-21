import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NurseBreadCrumbComponent } from './nurse-bread-crumb.component';

describe('NurseBreadCrumbComponent', () => {
  let component: NurseBreadCrumbComponent;
  let fixture: ComponentFixture<NurseBreadCrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NurseBreadCrumbComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NurseBreadCrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
