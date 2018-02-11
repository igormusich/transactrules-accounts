import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  MatInputModule, 
  MatPaginatorModule, 
  MatProgressSpinnerModule, 
  MatSortModule, 
  MatTableModule, 
  MatButtonModule,
  MatCheckboxModule,
  MatSnackBarModule, 
  MatDialogModule,
  MatIconModule,
  MatMenuModule,
} from '@angular/material';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReactiveFormsModule } from '@angular/forms';
import { BreadcrumbsModule } from '../../../core/breadcrumbs/breadcrumbs.module';
import { CdkTableModule } from '@angular/cdk/table';
import { ScrollbarModule } from '../../../core/scrollbar/scrollbar.module';
import { PageHeaderModule } from '../../../core/page-header/page-header.module';
import { ListModule } from '../../../core/list/list.module';
import { InputFileModule } from '../../../core/input-file/input-file.module';

import { AccountTypeRoutingModule } from './account-type-routing.module';
import { AccountTypeComponent } from 'app/pages/settings/account-type/account-type.component';
import { CreateAccountTypeComponent } from 'app/pages/settings/account-type/create-account-type/create-account-type.component';
import { ImportAccountComponent } from './import-account/import-account.component';

@NgModule({
  imports: [
    CommonModule,
    AccountTypeRoutingModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    ScrollbarModule,
    PageHeaderModule,
    BreadcrumbsModule,
    ListModule,
    InputFileModule,
    CdkTableModule,
    MatTableModule,
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule, 
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  declarations: [AccountTypeComponent,CreateAccountTypeComponent, ImportAccountComponent],
  exports: [
    AccountTypeComponent
  ],
  entryComponents: [CreateAccountTypeComponent, ImportAccountComponent],
})
export class AccountTypeModule { }
