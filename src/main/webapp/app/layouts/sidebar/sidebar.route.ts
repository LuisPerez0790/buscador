import { Route } from '@angular/router';

import { SidebarComponent } from 'app/layouts/sidebar/sidebar.component';

export const sidebarRoute: Route = {
    path: '',
    component: SidebarComponent,
    outlet: 'sidebar'
};
