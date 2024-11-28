import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatbotComponent } from './chatbot.component';
import { ChatbotIndexComponent } from './chatbot-index/chatbot-index.component';

const routes: Routes = [
  {
    path: '',
    component: ChatbotComponent,
    children: [
      {
        path: '',
        component: ChatbotIndexComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChatbotRoutingModule { }
