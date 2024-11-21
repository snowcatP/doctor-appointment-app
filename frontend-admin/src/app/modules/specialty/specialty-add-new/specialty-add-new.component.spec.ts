import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecialtyAddNewComponent } from './specialty-add-new.component';

describe('SpecialtyAddNewComponent', () => {
  let component: SpecialtyAddNewComponent;
  let fixture: ComponentFixture<SpecialtyAddNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpecialtyAddNewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SpecialtyAddNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
