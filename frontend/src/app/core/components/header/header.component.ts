import { Component, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { MenuItem } from 'primeng/api';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {

  constructor(public accessChecker: NbAccessChecker) {}

  ngOnInit(): void {
    
  }

}
