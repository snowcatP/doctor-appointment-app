import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { host } from '../../../environments/environment';
import { Observable } from 'rxjs';
import {
  ChatMessageResponse,
  ConversationResponse,
  CreateConversationRequest,
} from '../models/chat.model';

@Injectable({
  providedIn: 'root',
})
export class ConversationService {
  private baseUrl = `${host}/api/conversation`;

  constructor(private http: HttpClient) {}

  createNewConversation(
    request: CreateConversationRequest
  ): Observable<ConversationResponse> {
    return this.http.post<ConversationResponse>(
      `${this.baseUrl}/create`,
      request
    );
  }

  getAllConversationsByUser(): Observable<ConversationResponse[]> {
    return this.http.get<ConversationResponse[]>(
      `${this.baseUrl}/get-all-by-user`
    );
  }

  getDataOfConversation(
    conversationId: number
  ): Observable<ChatMessageResponse[]> {
    return this.http.get<ChatMessageResponse[]>(
      `${this.baseUrl}/get-data-of-conversation/${conversationId}`
    );
  }
}
