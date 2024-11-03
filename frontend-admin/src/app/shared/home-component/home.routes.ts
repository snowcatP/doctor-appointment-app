import { Routes } from '@angular/router';
import { StarterComponent } from '../starter/starter.component';

export const HomeRoute: Routes = [
    {
        path: '',
        component: StarterComponent,
        data: {
          title: 'Starter',
          urls: [
            { title: 'Dashboard', url: '/dashboard' },
            { title: 'Starter' },
          ],
        },
      },
]