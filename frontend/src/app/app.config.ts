import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { JwtHelperService, JWT_OPTIONS  } from '@auth0/angular-jwt';
import { NbRoleProvider, NbSecurityModule } from '@nebular/security';
import { RoleProviderService } from './services/role-provider.service';
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    provideAnimationsAsync(),
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService,
    NbSecurityModule.forRoot({
      accessControl: {
        guest: {
          view: '*',
        },
        PATIENT: {
          parent: 'guest',
          create: '*',
          edit: '*',
          remove: '*',
        },
        DOCTOR:{
          parent: 'guest',
          create: '*',
          edit: '*',
          remove: '*',
          doctor: '*',

        }
      },
    }).providers,
    { provide: NbRoleProvider, useClass: RoleProviderService },

    
    // importProvidersFrom(
    //   NbThemeModule.forRoot({ name: 'default' }),
    //   NbToastrModule.forRoot({
    //     position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
    //     preventDuplicates: true 
    //   }),
      
    // ),
  ],
};
