import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EditAccountDetailsComponent } from 'app/pages/data/create-account/edit-account-details/edit-account-details.component';
import { EditAccountSchedulesComponent } from 'app/pages/data/create-account/edit-account-schedules/edit-account-schedules.component';
import { EditAccountInstalmentsComponent } from 'app/pages/data/create-account/edit-account-instalments/edit-account-instalments.component';


const routes: Routes = [
  {
    path: '',
    component: EditAccountDetailsComponent
  },
  {
    path: 'schedules',
    component: EditAccountSchedulesComponent
  },
  {
    path: 'instalments',
    component: EditAccountInstalmentsComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreateAccountRoutingModule { }
