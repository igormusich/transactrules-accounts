import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { CdkTableModule } from '@angular/cdk/table'
import { OverlayModule } from '@angular/cdk/overlay';
import { ReactiveFormsModule } from '@angular/forms';
import { InputFileComponent } from './input-file.component';
import { ByteFormatPipe } from './byte-format.pipe';
@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [InputFileComponent,ByteFormatPipe],
  exports: [InputFileComponent,ByteFormatPipe]
})
export class InputFileModule { }
