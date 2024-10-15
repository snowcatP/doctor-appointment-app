import { CommonModule } from '@angular/common';
import { Component,  OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterModule, RouterOutlet } from '@angular/router';
import { NbAccessChecker } from '@nebular/security';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterOutlet, RouterModule, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  ngOnInit(): void {
      
  }
 
  constructor(public accessChecker: NbAccessChecker) { }
}
