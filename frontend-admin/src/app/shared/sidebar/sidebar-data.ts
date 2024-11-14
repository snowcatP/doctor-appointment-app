import { NavItem } from "../../core/models/nav-item";

export const navItems: NavItem[] = [
  {
    navCap: 'Home',
  },
  {
    displayName: 'Dashboard',
    iconName: 'layout-dashboard',
    bgcolor: 'primary',
    route: '/dashboard',
  },
  {
    displayName: 'Appointments',
    iconName: 'calendar',
    bgcolor: 'primary',
    route: '/ui-components/badge',
  },
  {
    displayName: 'Specialty',
    iconName: 'hospital',
    bgcolor: 'primary',
    route: '/specialty',
  },
  {
    displayName: 'Doctors',
    iconName: 'stethoscope',
    bgcolor: 'primary',
    route: '/doctor',
  },
  {
    displayName: 'Patients',
    iconName: 'wheelchair',
    bgcolor: 'primary',
    route: '/ui-components/chips',
  },
  {
    displayName: 'Schedules',
    iconName: 'clock-hour-5',
    bgcolor: 'primary',
    route: '/ui-components/lists',
  },
  {
    displayName: 'Medical Records',
    iconName: 'clipboard-plus',
    bgcolor: 'primary',
    route: '/ui-components/menu',
  },
  {
    displayName: 'Notifications',
    iconName: 'bell-ringing',
    bgcolor: 'primary',
    route: '/ui-components/tooltips',
  },
  {
    displayName: 'Setting',
    iconName: 'settings',
    bgcolor: 'primary',
    route: '/ui-components/tooltips',
  },
  {
    displayName: 'Logout',
    iconName: 'lock',
    bgcolor: 'warning',
    route: '/auth/logout',
  },
  {
    displayName: 'Register',
    iconName: 'user-plus',
    bgcolor: 'success',
    route: '/auth/register',
  },
  
];
