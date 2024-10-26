import { Component } from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
@Component({
  selector: 'app-search-doctor-bread-crumb',
  templateUrl: './search-doctor-bread-crumb.component.html',
  styleUrl: './search-doctor-bread-crumb.component.css'
})
export class SearchDoctorBreadCrumbComponent {
  selected = 'option2';

}
