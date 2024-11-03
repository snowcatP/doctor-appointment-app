import { Component, ViewEncapsulation } from '@angular/core';
import { MaterialModule } from '../../../material.module';

@Component({
  selector: 'app-starter',
  templateUrl: './starter.component.html',
  standalone:true,
  imports: [
    MaterialModule,

  ],
  encapsulation: ViewEncapsulation.None,
})
export class StarterComponent {

}
