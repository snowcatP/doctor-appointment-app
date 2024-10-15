import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, RouterModule } from '@angular/router';
import { HeaderComponent } from './client/master-layout/header/header.component';
import { FooterComponent } from './client/master-layout/footer/footer.component';
import { HomeComponent } from './client/master-layout/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormConfig } from '@rxweb/reactive-form-validators';
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
  ],
  providers: [JwtHelperService,{ provide: JWT_OPTIONS, useValue: JWT_OPTIONS },],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
  ngOnInit(): void {
    // this.settingService.getRequestConfigCompany();
    ReactiveFormConfig.set({
      internationalization: {
        dateFormat: 'dmy',
        seperator: '/'
      },
      validationMessage: {
        
        alpha: 'Only alphabelts are allowed.',
        alphaNumeric: 'Only alphabet and numbers are allowed.',
        compare: 'Input are not matched',
        contains: 'value is not contains in the input',
        creditcard: 'creditcard number is not correct',
        digit: 'Only digit are allowed',
        email: 'Email is not valid',
        greaterThanEqualTo: 'please enter greater than or equal to the joining age',
        greaterThan: 'please enter greater than to the joining age',
        hexColor: 'please enter hex code',
        json: 'Please enter valid json',
        lessThanEqualTo: 'Please enter less than or equal to the current experience',
        lessThan: 'Please enter less than or equal to the current experience',
        lowerCase: 'Only lowercase is allowed',
        maxLength: 'Maximum length is {{1}} characters',
        minLength: 'Minimum length is {{1}} characters',
        maxNumber: 'Enter value less than or equal to {{1}}',
        minNumber: 'Enter value greater than or equal to {{1}}',
        password: 'Please enter valid password with letter, number and special charater(Ex: @, #, &,...)',
        pattern: 'Please enter valid zipcode',
        range: 'Please enter value between {{1}} to {{2}}',
        required: 'This field is required',
        time: 'Only time format is allowed',
        upperCase: 'Only uppercase is allowed',
        url: 'Only url format is allowed',
        zipCode: 'Enter valid zip code',
        numberic: 'Only numeric is allowed',
        date: 'Please enter valid date format'
      }
    });


  }
}
