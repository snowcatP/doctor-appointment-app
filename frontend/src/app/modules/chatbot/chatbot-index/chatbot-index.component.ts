import { Component, ElementRef, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
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
  @Input('mode') isChatbotMode: boolean;
  @Output('toggleChatbot') toggleChatbot: EventEmitter<any> = new EventEmitter();
  @ViewChild('chatScroll') chatScroll: ElementRef<any>;
  openChat: boolean = false;
  isSending: boolean = false;
  messages: ChatbotMessage[] = [];
  prompt: string = '';
  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.promptInput.nativeElement.focus();
  }

  close() {
    this.toggleChatbot.emit('false');
  }

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
    this.scrollToBottom();
    this.promptInput.nativeElement.value = '';
    this.chatbotService.sendPrompt(request).subscribe({
      next: (res) => {
        this.messages.push(res);
        this.scrollToBottom();
        this.isSending = false;
      },
      error: (err) => {
        console.log(err);
        this.isSending = false;
      }
    });
  }
  private scrollToBottom(): void {
    setTimeout(() => {
      try {
        this.chatScroll.nativeElement.scrollTop =
          this.chatScroll.nativeElement.scrollHeight;
      } catch (err) {
        console.error('Error scrolling to bottom:', err);
      }
    }, 100);
  }
}
