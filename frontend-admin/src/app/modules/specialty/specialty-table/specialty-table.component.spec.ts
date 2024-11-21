import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecialtyTableComponent } from './specialty-table.component';

describe('SpecialtyTableComponent', () => {
  let component: SpecialtyTableComponent;
  let fixture: ComponentFixture<SpecialtyTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpecialtyTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SpecialtyTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
