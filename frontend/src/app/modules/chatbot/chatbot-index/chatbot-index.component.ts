import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {
  ChatbotMessage,
  ChatbotRequest,
} from '../../../core/models/chatbot.model';
import { ChatbotService } from '../../../core/services/chatbot.service';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-chatbot-index',
  templateUrl: './chatbot-index.component.html',
  styleUrl: './chatbot-index.component.css',
})
export class ChatbotIndexComponent implements OnInit {
  @ViewChild('promptInput') promptInput: ElementRef;
  openChat: boolean = false;
  isSending: boolean = false;
  messages: ChatbotMessage[] = [];
  prompt: string = '';
  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {}

  refresh() {
    if (this.messages.length > 0)
      this.messages = [];
  }

  sendMessage() {
    this.isSending = true;
    const request: ChatbotRequest = {
      prompt: this.prompt,
    };
    const userPrompt: ChatbotMessage = {
      result: this.prompt,
      sender: 'USER'
    };
    this.messages.push(userPrompt);
    this.promptInput.nativeElement.value = '';
    this.chatbotService.sendPrompt(request).subscribe({
      next: (res) => {
        this.messages.push(res);
        this.isSending = false;
      },
      error: (err) => {
        console.log(err);
        this.isSending = false;
      }
    });
  }

}
