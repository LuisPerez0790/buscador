import { Route } from '@angular/router';

import { HomeComponent } from './';

export const HOME_ROUTE: Route = {
    path: 'home',
    component: HomeComponent,
    // redirectTo: '/sentence', pathMatch: 'full',
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
};
