import { Component } from '@angular/core';
import { MaterialModule } from '../../material.module';
import { ProfitExpensesComponent } from '../home-component/profit-expenses/profit-expenses.component';
import { TrafficDistributionComponent } from '../home-component/traffic-distribution/traffic-distribution.component';
import { ProductSalesComponent } from '../home-component/product-sales/product-sales.component';
import { UpcomingSchedulesComponent } from '../home-component/upcoming-schedules/upcoming-schedules.component';
import { TopEmployeesComponent } from '../home-component/top-employees/top-employees.component';
import { AppsBlogComponent } from '../home-component/apps-blog/apps-blog.component';

@Component({
  selector: 'app-starter',
  templateUrl: './starter.component.html',
  standalone: true,
  imports:[
    MaterialModule,
    ProfitExpensesComponent,
    TrafficDistributionComponent,
    ProductSalesComponent,
    UpcomingSchedulesComponent,
    TopEmployeesComponent,
    AppsBlogComponent

  ]
})
export class StarterComponent {

}
