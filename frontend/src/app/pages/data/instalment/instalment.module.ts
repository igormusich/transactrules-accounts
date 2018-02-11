import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { EditInstalmentComponent } from './edit-instalment/edit-instalment.component';
import { 
  MatInputModule, 
  MatButtonModule,
  MatCheckboxModule,
  MatDialogModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatSelectModule,
  MatIconModule
} from '@angular/material';


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule, 
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatIconModule,

    CommonModule
  ],
  exports: [EditInstalmentComponent],
  declarations: [EditInstalmentComponent],
  entryComponents :[EditInstalmentComponent]
})
export class InstalmentModule { }
