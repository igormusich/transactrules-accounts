import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccountTypeDetailsComponent } from 'app/pages/settings/account-type-details/account-type-details.component';

const routes: Routes = [
  {
    path: '',
    component: AccountTypeDetailsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountTypeDetailsRoutingModule { }
