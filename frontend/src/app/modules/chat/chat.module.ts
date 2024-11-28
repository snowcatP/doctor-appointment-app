import { AutoCompleteModule } from 'primeng/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatRoutingModule } from './chat-routing.module';
import { ChatIndexComponent } from './pages/chat-index/chat-index.component';
import { ChatComponent } from './chat.component';
import { RouterModule } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DialogModule } from 'primeng/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { MatTooltipModule } from '@angular/material/tooltip';
import { InputTextareaModule } from 'primeng/inputtextarea';

@NgModule({
  declarations: [ChatIndexComponent, ChatComponent],
  imports: [
    CommonModule,
    ChatRoutingModule,
    RouterModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    MatIconModule,
    MatButtonModule,
    FormsModule,
    MatProgressSpinnerModule,
    DialogModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,
    MatOptionModule,
    AutoCompleteModule,
    DropdownModule,
    ToastModule,
    MatTooltipModule,
    InputTextareaModule,
  ],
})
export class ChatModule {}
