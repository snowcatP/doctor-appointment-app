import { WebSocketService } from './core/services/webSocket.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ReactiveFormConfig } from '@rxweb/reactive-form-validators';
import { AuthService } from './core/services/auth.service';
import { Store } from '@ngrx/store';
import * as AuthActions from './core/states/auth/auth.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit, OnDestroy{
  title = 'frontend';
  visibleChatbot: boolean = false;
  mode: string = 'window';
  loadChatbot: boolean = false;
  constructor(
    private authService: AuthService,
    private store: Store,
    private WebSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.authService.getUserData().subscribe({
        next: (res) => {
          this.store.dispatch(
            AuthActions.loadProfile({
              loginSuccessResponse: {
                user: res,
                token: localStorage.getItem('token'),
                isAuthenticated: true,
              },
            })
          );
        },
        error: (err) => {
          this.store.dispatch(AuthActions.loginFailure({ error: err }));
        },
      });
    }
    ReactiveFormConfig.set({
      internationalization: {
        dateFormat: 'dmy',
        seperator: '/',
      },
      validationMessage: {
        alpha: 'Only alphabelts are allowed.',
        alphaNumeric: 'Only alphabet and numbers are allowed.',
        compare: 'Input are not matched',
        contains: 'value is not contains in the input',
        creditcard: 'creditcard number is not correct',
        digit: 'Only digit are allowed',
        email: 'Email is not valid',
        greaterThanEqualTo:
          'please enter greater than or equal to the joining age',
        greaterThan: 'please enter greater than to the joining age',
        hexColor: 'please enter hex code',
        json: 'Please enter valid json',
        lessThanEqualTo:
          'Please enter less than or equal to the current experience',
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
        date: 'Please enter valid date format',
      },
    });

    this.WebSocketService.connectSocket();
  }

  ngOnDestroy(): void {
    this.WebSocketService.disconnectSocket();
  }

  toggleChatbot() {
    this.visibleChatbot = !this.visibleChatbot;
    if (!this.loadChatbot) {
      this.loadChatbot = true;
    }
  }

  listenToggleChatbot(event: any) {
    this.visibleChatbot = !this.visibleChatbot;
  }
}
