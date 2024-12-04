import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-doctor-bread-crumb',
  templateUrl: './doctor-bread-crumb.component.html',
  styleUrls: ['./doctor-bread-crumb.component.css']
})
export class DoctorBreadCrumbComponent implements OnInit {
  breadcrumbTitle: string = 'Dashboard';

  // Define a route-to-title mapping, including nested paths
  routeTitles: { [key: string]: string } = {
    '/': 'Home',
    '/doctor/dashboard': 'Dashboard',
    '/doctor/review': 'Review',
    '/doctor/myPatient': 'My Patients',
    '/doctor/profile': 'Doctor Profile',
    '/doctor/patient-profile': 'Patient Profile',
    '/doctor/appointment': 'Appointments',
    '/doctor/change-password': 'Change Password',
    // Add more paths as needed
  };

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    // Subscribe to router events to update breadcrumb title on navigation
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        map(() => this.buildRoutePath(this.activatedRoute))
      )
      .subscribe((routePath: string) => { 
        this.breadcrumbTitle = this.routeTitles[routePath] || 'Dashboard';
      });
  }

  // Helper method to build the full route path
  private buildRoutePath(route: ActivatedRoute): string {
    let path = route.snapshot.url.map(segment => segment.path).join('/');
    if (path) path = '/' + path;

    while (route.firstChild) {
      route = route.firstChild;
      const childPath = route.snapshot.url.map(segment => segment.path).join('/');
      if (childPath) {
        path += '/' + childPath;
      }
    }

    return path.startsWith('/doctor') ? path : '/doctor' + path; // Ensure the path starts with '/doctor'
  }
}
