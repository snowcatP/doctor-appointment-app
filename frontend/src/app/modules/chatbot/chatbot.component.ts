import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.css'
})
export class ChatbotComponent implements OnInit{
  @Input('visibleChatbot') visibleChatbot: boolean;
  @Input('mode') mode: string;
  @Output('toggleChatbot') toggleChatbot = new EventEmitter();
  isChatbotMode: boolean;
  openChat: boolean = false;


  ngOnInit(): void {
    this.isChatbotMode = this.mode == 'window';
  }

  toggleChatbotListener(event: any) {
    this.toggleChatbot.emit(event)
  }
}
