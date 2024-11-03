import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../../core/services/account.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
})
export class LogoutComponent implements OnInit{
  constructor(private accountService: AccountService, private router: Router, private messageService: MessageService){}
  ngOnInit(): void {
    let token = localStorage.getItem('token')!;
    this.accountService.onLogout(token).subscribe({
      next: (res:any) => {
        localStorage.removeItem('token'),
        this.router.navigateByUrl('')
        this.messageService.add({key:'messageToast' ,severity: 'success',summary:'Success', detail: `Logout successfully!` })


      }
    })
  }

}
