import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChatbotMessage, ChatbotRequest } from '../models/chatbot.model';
import { Observable } from 'rxjs';
import { host } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private baseUrl: string = `${host}/api/ai/chatbot`;

  constructor(private http: HttpClient) { }

  sendPrompt(request: ChatbotRequest): Observable<ChatbotMessage> {
    return this.http.post<ChatbotMessage>(this.baseUrl, request);
  }
}
