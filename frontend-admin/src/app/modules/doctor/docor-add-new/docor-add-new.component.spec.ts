import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocorAddNewComponent } from './docor-add-new.component';

describe('DocorAddNewComponent', () => {
  let component: DocorAddNewComponent;
  let fixture: ComponentFixture<DocorAddNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocorAddNewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocorAddNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
