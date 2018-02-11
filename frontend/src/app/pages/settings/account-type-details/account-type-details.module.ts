import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import {

  MatPaginatorModule, 
  MatProgressSpinnerModule, 
  MatSortModule, 
  MatDialogModule,
  MatButtonModule, 
  MatCheckboxModule, 
  MatDatepickerModule, 
  MatIconModule, 
  MatInputModule,
  MatMenuModule, 
  MatNativeDateModule, 
  MatRadioModule,
  MatSnackBarModule, 
  MatSelectModule,
  MatSliderModule,
  MatSlideToggleModule, 
  MatTabsModule,
  MatTableModule,
  MatTooltipModule
} from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import { UtilsModule } from '../../../core/utils/utils.module';
import { PageHeaderModule } from '../../../core/page-header/page-header.module';
import { ListModule } from '../../../core/list/list.module';
import { CdkTableModule } from '@angular/cdk/table';

import { AccountTypeDetailsRoutingModule } from './account-type-details-routing.module';
import { BreadcrumbsModule } from '../../../core/breadcrumbs/breadcrumbs.module';
import { AccountTypeDetailsComponent } from 'app/pages/settings/account-type-details/account-type-details.component';
import { PositionTypeDetailsComponent } from './position-type-details/position-type-details.component';

@NgModule({
  imports: [
      CommonModule,
      ReactiveFormsModule,
      ListModule,
      BreadcrumbsModule,
      FlexLayoutModule,
      CdkTableModule,
      MatPaginatorModule, 
      MatProgressSpinnerModule, 
      MatSortModule, 
      MatDialogModule,
      MatButtonModule, 
      MatCheckboxModule, 
      MatDatepickerModule, 
      MatIconModule, 
      MatInputModule,
      MatMenuModule, 
      MatNativeDateModule, 
      MatRadioModule, 
      MatSelectModule,
      MatSliderModule,
      MatSlideToggleModule,
      MatSnackBarModule, 
      MatTabsModule,
      MatTableModule,
      MatTooltipModule,
      PageHeaderModule,
      AccountTypeDetailsRoutingModule
  ],
  entryComponents: [PositionTypeDetailsComponent],
  declarations: [AccountTypeDetailsComponent, PositionTypeDetailsComponent],
  exports: [AccountTypeDetailsComponent]
})
export class AccountTypeDetailsModule { }
