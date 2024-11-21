import { WebSocketService } from './../../../../core/services/webSocket.service';
import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  filter,
  map,
  Observable,
  startWith,
  Subject,
  Subscription,
} from 'rxjs';
import {
  ChatMessageRequest,
  ChatMessageResponse,
  ConversationResponse,
  CreateConversationRequest,
} from '../../../../core/models/chat.model';
import { DoctorService } from '../../../../core/services/doctor.service';
import { DoctorChatResponse } from '../../../../core/models/doctor.model';
import { ConversationService } from '../../../../core/services/conversation.service';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../../../core/states/auth/auth.reducer';
import { User } from '../../../../core/models/authentication.model';
import { ConnectedOverlayScrollHandler } from 'primeng/dom';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-chat-index',
  templateUrl: './chat-index.component.html',
  styleUrl: './chat-index.component.css',
})
export class ChatIndexComponent implements OnInit, OnDestroy {
  @ViewChild('messageInput') messageInput: ElementRef;
  @ViewChild('chatScroll') chatScroll: ElementRef;
  message: string = '';
  isSending: boolean = false;
  conversationId = 'f83011f7-d037-4eee-9212-1c3696dbfeee';
  conversations: ConversationResponse[];
  selectedConversation: ConversationResponse;
  selectedConversationData: ChatMessageResponse[];
  visible: boolean = false;
  doctor: DoctorChatResponse;
  doctors: DoctorChatResponse[];
  doctorsFiltered: Observable<DoctorChatResponse[]>;
  isLoading: boolean = false;
  isFetchedDoctors: boolean = false;
  isLoadingConversations: boolean = false;
  isLoadingDataConversation: boolean = false;
  currentUser: User;
  private chatSubscription: Subscription;
  private unsubscribe$ = new Subject<void>();

  constructor(
    private webSocketService: WebSocketService,
    private doctorService: DoctorService,
    private conversationService: ConversationService,
    private store: Store<fromAuth.State>,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.webSocketInit();
    this.handleObservable();
  }

  ngOnDestroy(): void {
    if (this.chatSubscription) {
      this.chatSubscription.unsubscribe();
    }
    this.webSocketService.disconnectSocket();
    this.unsubscribe$.next();
    this.unsubscribe$.complete(); // Cleanup subscription on component destroy
  }

  handleObservable() {
    this.store.select(fromAuth.selectUser).subscribe((user) => {
      this.currentUser = user;
    });
  }

  createNewConversation() {
    this.isLoading = true;
    const isPatient = this.currentUser.role.roleName === 'PATIENT';
    const request: CreateConversationRequest = {
      doctorEmail: isPatient ? this.doctor.email : this.currentUser.email,
      patientEmail: isPatient ? this.currentUser.email : this.doctor.email,
    };
    this.conversationService.createNewConversation(request).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Create new Conversation successfully.',
        });
        this.getData();
        this.visible = false;
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Create new Conversation fail.',
        });
      },
    });
    this.isLoading = false;
  }

  getData() {
    this.isLoadingConversations = true;
    this.conversationService.getAllConversationsByUser().subscribe({
      next: (res) => {
        this.conversations = res;
        this.selectConversation(this.conversations[0]);
        this.isLoadingConversations = false;
      },
      error: (err) => {
        console.log(err);
        this.isLoadingConversations = false;
      },
    });
  }

  webSocketInit() {
    this.webSocketService
      .connectSocket()
      .pipe(filter((state) => state))
      .subscribe(() => {
        this.getData();
      });
  }

  sendMessage() {
    this.isSending = true;
    const data: ChatMessageRequest = {
      message: this.message,
      doctorEmail:
        this.currentUser.role.roleName == 'DOCTOR'
          ? this.currentUser.email
          : this.selectedConversation.doctor.email,
      patientEmail:
        this.currentUser.role.roleName == 'PATIENT'
          ? this.currentUser.email
          : this.selectedConversation.patient.email,
      conversationId: this.selectedConversation.id,
      sender: this.currentUser.role.roleName
    };
    this.webSocketService.emit(
      `/app/chat/${this.selectedConversation.id}`,
      data
    );
    this.messageInput.nativeElement.value = '';
    this.isSending = false;
  }

  selectConversation(conversation: ConversationResponse) {
    this.selectedConversation = conversation;
    this.getConversationData();
    this.chatSubscription = this.webSocketService
      .on(`/user/chat/conversation/${this.selectedConversation.id}`)
      .subscribe((message: ChatMessageResponse) => {
        this.selectedConversationData.push(message);
        this.scrollToBottom();
      });
  }

  getConversationData() {
    this.isLoadingDataConversation = true;
    this.conversationService
      .getDataOfConversation(this.selectedConversation.id)
      .subscribe({
        next: (res) => {
          this.selectedConversationData = res;
          this.isLoadingDataConversation = false;
        },
        error: (err) => {
          console.log(err);
          this.isLoadingDataConversation = false;
        },
      });
  }

  isSentByCurrentUser(message: ChatMessageResponse): boolean {
    return message.sender === this.currentUser.role.roleName;
  }

  shouldShowAvatar(index: number): boolean {
    if (index === 0) {
      return true;
    }
    const currentMessage = this.selectedConversationData[index];
    const previousMessage = this.selectedConversationData[index - 1];
    return currentMessage.sender != previousMessage.sender;
  }

  openCreateNewChatDialog() {
    if (!this.isFetchedDoctors) {
      this.doctorService.getAllDoctorsHaveBookedByPatient().subscribe({
        next: (res) => {
          this.doctors = res;
          this.isFetchedDoctors = true;
        },
        error: (err) => console.log(err),
      });
    }
    this.visible = true;
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    try {
      this.chatScroll.nativeElement.scrollTop =
        this.chatScroll.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Error scrolling to bottom:', err);
    }
  }
}
