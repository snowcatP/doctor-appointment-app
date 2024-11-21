import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatIndexComponent } from './chat-index.component';

describe('ChatIndexComponent', () => {
  let component: ChatIndexComponent;
  let fixture: ComponentFixture<ChatIndexComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChatIndexComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
