import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './core/layout/layout.component';
import { authRoutes } from './pages/auth/auth.routing';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [

      {
        path: '',
        loadChildren: 'app/pages/data/accounts/accounts.module#AccountsModule',
        pathMatch: 'full'
      },
      {
        path: 'data/create-account',
        loadChildren: 'app/pages/data/create-account/create-account.module#CreateAccountModule',
      },
      {
        path: 'data/account-details/:accountNumber',
        loadChildren: 'app/pages/data/account-details/account-details.module#AccountDetailsModule',
      },
      {
        path: 'data/calendars',
        loadChildren: 'app/pages/data/calendar/calendar.module#CalendarModule',
      },
      {
        path: 'settings/account-types',
        loadChildren: 'app/pages/settings/account-type/account-type.module#AccountTypeModule'
      },
      {
        path: 'settings/account-type-details',
        loadChildren: 'app/pages/settings/account-type-details/account-type-details.module#AccountTypeDetailsModule'
      }
    ]
  },
  {
    path: 'auth',
    children: [
      ...authRoutes
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
