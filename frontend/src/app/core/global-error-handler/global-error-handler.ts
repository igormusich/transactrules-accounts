import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';

import { Subject } from 'rxjs/Subject';

import { ErrorContext } from './error-context';

@Injectable()
export class GlobalErrorHandler extends ErrorHandler {

  private emptyErrorContext: ErrorContext = {
   message: '',
   location: ''
  };

  private errorContextSubject = new Subject<ErrorContext>();
  errorContext$ = this.errorContextSubject.asObservable();

  /**
   * Since error handling is really important it needs to be loaded first,
   * thus making it not possible to use dependency injection in the constructor
   * to get other services such as the error handle API service to send the server
   * our error details
   * @param injector
   */
  constructor( private injector: Injector ) {
    super(  );
    this.errorContextSubject.next( this.emptyErrorContext );
  }

  handleError( error ) {
    const locationStrategy = this.injector.get( LocationStrategy );
    let nextErrorContext: ErrorContext = {
      message: error.message ? error.message : error.toString(),
      location: locationStrategy instanceof PathLocationStrategy ? locationStrategy.path() : ''
    };
    this.errorContextSubject.next( nextErrorContext );
    super.handleError( error );
  }
}