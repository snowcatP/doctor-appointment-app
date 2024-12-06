import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatbotRoutingModule } from './chatbot-routing.module';
import { ChatbotIndexComponent } from './chatbot-index/chatbot-index.component';
import { ChatbotComponent } from './chatbot.component';
import { MatIconModule } from '@angular/material/icon';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'ngx-markdown';

@NgModule({
  declarations: [
    ChatbotIndexComponent,
    ChatbotComponent
  ],
  imports: [
    CommonModule,
    ChatbotRoutingModule,
    MatIconModule,
    OverlayPanelModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatTooltipModule,
    FormsModule,
    MarkdownModule.forChild(),
  ],
  exports: [
    ChatbotIndexComponent,
    ChatbotComponent
  ],
})
export class ChatbotModule { }
