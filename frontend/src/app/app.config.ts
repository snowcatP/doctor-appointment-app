import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { OverlayContainer } from '@angular/cdk/overlay';
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    provideAnimationsAsync(),
    // importProvidersFrom(
    //   NbThemeModule.forRoot({ name: 'default' }),
    //   NbToastrModule.forRoot({
    //     position: NbGlobalPhysicalPosition.BOTTOM_RIGHT,
    //     preventDuplicates: true 
    //   }),
      
    // ),
  ],
};
