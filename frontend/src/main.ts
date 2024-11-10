import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient } from '@angular/common/http';

var global = window;
platformBrowserDynamic().bootstrapModule(AppModule, {
  providers:[provideAnimations()],
  ngZoneEventCoalescing: true
})
  .catch(err => console.error(err));
