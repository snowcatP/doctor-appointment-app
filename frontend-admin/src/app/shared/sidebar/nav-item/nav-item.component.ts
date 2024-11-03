import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import {
  Component,
  EventEmitter,
  HostBinding,
  Input,
  Output,
} from '@angular/core';
import { NavItem } from '../../../core/models/nav-item';
import { NavService } from '../../../core/services/nav.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-item',
  templateUrl: './nav-item.component.html',
  styleUrl: './nav-item.component.css',
  animations: [
    trigger('indicatorRotate', [
      state('collapsed', style({ transform: 'rotate(0deg)' })),
      state('expanded', style({ transform: 'rotate(180deg)' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ]),
  ],
})
export class NavItemComponent {
  @Output() toggleMobileLink: any = new EventEmitter<void>();
  @Output() notify: EventEmitter<boolean> = new EventEmitter<boolean>();

  expanded: any = false;
  disabled: any = false;
  twoLines: any = false;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @Input() item: NavItem | any;
  @Input() depth: any;

  constructor(public navService: NavService, public router: Router) {
    if (this.depth === undefined) {
      this.depth = 0;
    }
  }

  ngOnChanges() {
    this.navService.currentUrl.subscribe((url: string) => {
      if (this.item.route && url) {
        // console.log(`Checking '/${this.item.route}' against '${url}'`);
        this.expanded = url.indexOf(`/${this.item.route}`) === 0;
        this.ariaExpanded = this.expanded;
        //console.log(`${this.item.route} is expanded: ${this.expanded}`);
      }
    });
  }

  onItemSelected(item: NavItem) {
    if (!item.children || !item.children.length) {
      this.router.navigate([item.route]);
    }
    if (item.children && item.children.length) {
      this.expanded = !this.expanded;
    }
    //scroll
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
    if (!this.expanded) {
      if (window.innerWidth < 1024) {
        this.notify.emit();
      }
    }
  }
  onSubItemSelected(item: NavItem) {
    if (!item.children || !item.children.length){
      if (this.expanded && window.innerWidth < 1024) {
        this.notify.emit();
      }
    }
  }
}
