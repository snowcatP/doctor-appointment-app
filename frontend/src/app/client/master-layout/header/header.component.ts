import { Component,  OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  ngOnInit(): void {
      
  }
 
  constructor(
    public accessChecker: NbAccessChecker

  ) { }
}
