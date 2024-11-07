import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ToastModule } from 'primeng/toast';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './shared/header/header.component';
import { RouterModule } from '@angular/router';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { CommonModule, NgIf } from '@angular/common';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from './material.module';
import { HomeComponent } from './shared/home/home.component';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { BrandingComponent } from './shared/sidebar/branding.components';
import { NavItemComponent } from './shared/sidebar/nav-item/nav-item.component';
import { TranslateModule } from '@ngx-translate/core';
import * as TablerIcons from 'angular-tabler-icons/icons';
import { MessageService } from 'primeng/api';
import { StarterComponent } from './shared/starter/starter.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { provideAnimations } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { httpErrorInterceptor } from './core/interceptors/http-error.interceptor';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    SidebarComponent,
    NavItemComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ToastModule,
    RouterModule,
    NgScrollbarModule,
    CommonModule,
    MaterialModule,
    BrandingComponent, 
    TranslateModule,
    HttpClientModule,
    TablerIconsModule.pick(TablerIcons),
    NgApexchartsModule,
    TranslateModule.forRoot(),
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    ReactiveFormsModule
    

    ],
  providers: [
    MessageService,
    provideAnimations(),
    provideHttpClient(
      withInterceptors([httpErrorInterceptor, authInterceptor])
    ),
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService,
    HttpClientModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
