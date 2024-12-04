import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.css'
})
export class ChatbotComponent implements OnInit{
  @Input('visibleChatbot') visibleChatbot: boolean;
  @Input('mode') mode: string;
  openChat: boolean = false;


  ngOnInit(): void {
    
  }
}
