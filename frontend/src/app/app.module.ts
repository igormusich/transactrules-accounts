import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { environment } from '../environments/environment';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PagesModule } from './pages/pages.module';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { reducers } from './reducers';
import { EffectsModule } from '@ngrx/effects';
import { RouteHandlerModule } from './core/route-handler/route-handler.module';
import { HttpClientModule } from '@angular/common/http';
import { ApiClientService } from 'app/api-client-service';
import { SelectedAccountTypeService } from './selected-account-type.service';
import { AccountCreateService } from './account-create.service';
@NgModule({
  imports: [
    BrowserModule.withServerTransition({ appId: 'elastic-ui' }),
    BrowserAnimationsModule,
    HttpClientModule,
    StoreModule.forRoot(reducers),
    !environment.production ? StoreDevtoolsModule.instrument({ maxAge: 50 }) : [],
    EffectsModule.forRoot([]),
    AppRoutingModule,
    CoreModule,
    PagesModule,
    RouteHandlerModule
  ],
  providers: [ApiClientService, SelectedAccountTypeService,AccountCreateService],
  declarations: [AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
