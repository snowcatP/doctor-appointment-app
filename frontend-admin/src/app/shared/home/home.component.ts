import { Component, ViewChild } from '@angular/core';
import { navItems } from '../sidebar/sidebar-data';
import { MatSidenav } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { BreakpointObserver } from '@angular/cdk/layout';
import { NavService } from '../../core/services/nav.service';
import { MenuItem, MessageService } from 'primeng/api';

const MOBILE_VIEW = 'screen and (max-width: 768px)';
const TABLET_VIEW = 'screen and (min-width: 769px) and (max-width: 1024px)';
const MONITOR_VIEW = 'screen and (min-width: 1024px)';
const BELOWMONITOR = 'screen and (max-width: 1023px)';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent {

  navItems = navItems;
  items: MenuItem[];

  @ViewChild('leftsidenav')
  public sidenav: MatSidenav | any;

  //get options from service
  private layoutChangesSubscription = Subscription.EMPTY;
  private isMobileScreen = false;
  private isContentWidthFixed = true;
  private isCollapsedWidthFixed = false;
  private htmlElement!: HTMLHtmlElement;

  get isOver(): boolean {
    return this.isMobileScreen;
  }

  constructor(private breakpointObserver: BreakpointObserver, private navService: NavService) {

    this.htmlElement = document.querySelector('html')!;
    this.htmlElement.classList.add('light-theme');
    this.layoutChangesSubscription = this.breakpointObserver
      .observe([MOBILE_VIEW, TABLET_VIEW, MONITOR_VIEW])
      .subscribe((state) => {
        // SidenavOpened must be reset true when layout changes

        this.isMobileScreen = state.breakpoints[MOBILE_VIEW];

        this.isContentWidthFixed = state.breakpoints[MONITOR_VIEW];
      });
  }

  ngOnInit(): void { 
    this.items =[
      {
        label:'Dashboard',
        icon: 'fas fa-tachometer-alt',
        route: '/dashboard',
        topmenu: true
      },
      {
        label:'Appointments',
        icon: 'far fa-calendar-check',
        route: '/dashboard',
        topmenu: true
      },
      {
        label:'Specialty',
        icon: 'fa fa-hospital',
        topmenu: true,
        items:[
          {
            label:'Specialty List',
            route: '/specialty',
          },
          {
            label:'Add New Specialty',
            route: '/specialty/add-specialty',
          },
          {
            label:'Edit Specialty',
            route: '/specialty/edit-specialty',
          }
        ]
      },
      {
        label: 'Doctors',
        icon: 'fa fa-user-md',
        topmenu: true,
        items:[
          {
            label:'Doctor List',
            route: '/doctor',
          },
          {
            label:'Add New Doctor',
            route: '/doctor/add-doctor',
          },
          {
            label:'Edit Doctor',
            route: '/doctor/edit-doctor',
          }
        ]
      },
      {
        label: 'Patient',
        icon: 'fa fa-users',
        topmenu: true,
        items:[
          {
            label:'Patient List',
            route: '/patient',
          },
        ]
      },
      {
        label: 'Schedules',
        icon: 'fas fa-calendar-alt',
        topmenu: true,
        // items:[
        //   {
        //     label:'Doctor List',
        //     route: '/doctor',
        //   },
        //   {
        //     label:'Add New Doctor',
        //     route: '/doctor',
        //   },
        //   {
        //     label:'Edit Doctor',
        //     route: '/doctor',
        //   }
        // ]
      },
    ]
  }

  ngOnDestroy() {
    this.layoutChangesSubscription.unsubscribe(); 
  }

  toggleCollapsed() {
    this.isContentWidthFixed = false;
  }

  onSidenavClosedStart() {
    this.isContentWidthFixed = false;
  }

  onSidenavOpenedChange(isOpened: boolean) {
    this.isCollapsedWidthFixed = !this.isOver;
  }
}
