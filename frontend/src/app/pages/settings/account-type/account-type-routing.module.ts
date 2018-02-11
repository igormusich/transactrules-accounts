import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccountTypeComponent } from 'app/pages/settings/account-type/account-type.component';

const routes: Routes = [
  {
    path: '',
    component: AccountTypeComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountTypeRoutingModule { }
