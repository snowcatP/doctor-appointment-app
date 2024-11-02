import { Component, OnInit , Input} from '@angular/core';
import { ReactiveFormConfig } from '@rxweb/reactive-form-validators';
import { AuthService } from './core/services/auth.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit{
  title = 'frontend';
  isLoggedIn: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {

    this.authService.currentLoginStatus.subscribe(status => {
      this.isLoggedIn = status;
    });
    
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
        maxNumber: 'Enter value less than or equal to {{1}}',
        minNumber: 'Enter value greater than or equal to {{1}}',
        password: 'Please enter valid password',
        pattern: 'Please enter valid zipcode',
        range: 'Please enter value between {{1}} to {{2}}',
        required: 'This field is required',
        time: 'Only time format is allowed',
        upperCase: 'Only uppercase is allowed',
        url: 'Only url format is allowed',
        zipCode: 'Enter valid zip code',
        minLength: 'Minimum length is {{1}} characters',
        numberic: 'Only numeric is allowed',
        date: 'Please enter valid date format'
      }
    });
  }


  
}
