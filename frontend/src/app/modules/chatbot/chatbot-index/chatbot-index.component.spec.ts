import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatbotIndexComponent } from './chatbot-index.component';

describe('ChatbotIndexComponent', () => {
  let component: ChatbotIndexComponent;
  let fixture: ComponentFixture<ChatbotIndexComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChatbotIndexComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatbotIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
