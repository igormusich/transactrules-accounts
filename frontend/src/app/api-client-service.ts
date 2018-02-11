import { Inject, Injectable, Optional } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import {
  Account,
  AccountType,
  AmountType,
  AmountValue,
  Calendar,
  DateType,
  DateValue,
  HolidayDate,
  InstalmentSet,
  InstalmentType,
  InstalmentValue,
  OptionType,
  OptionValue,
  Position,
  PositionType,
  RateType,
  RateValue,
  Schedule,
  ScheduleDate,
  ScheduleType,
  ScheduledTransaction,
  Transaction,
  TransactionRuleType,
  TransactionType
} from './models';
import { CalendarComponent } from 'app/pages/data/calendar/calendar.component';
import { environment } from 'environments/environment';

/**
* Created with angular4-swagger-client-generator.
*/
@Injectable()
export class ApiClientService {

  private domain = environment.apiurl;

  constructor(private http: HttpClient, @Optional() @Inject('domain') domain: string ) {
    if (domain) {
      this.domain = domain;
    }
  }

  /**
  * Method findAllAccountTypes
  * @return Full HTTP response as Observable
  */
  public findAllAccountTypes(): Observable<AccountType[]> {
    let uri = `/accountTypes`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    //return this.sendRequest<AccountType[]>('get', uri, headers, params, null);
    return this.http.get<AccountType[]>(this.domain +  uri);
  }

    /**
  * Method findByNameUsingGET
  * @param className className
  * @return Full HTTP response as Observable
  */
  public getAccountType(className: string): Observable<HttpResponse<AccountType>> {
    let uri = `/accountTypes/${className}`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<AccountType>('get', uri, headers, params, null);
  }

  /**
  * Method findAccountByAccountNumber
  * @param className className
  * @return HTTP response as Observable
  */
  public deleteAccountTypeByClassName(className: string): Observable<HttpResponse<any>> {
    let uri = `/accountTypes/${className}`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<AccountType[]>('delete', uri, headers, params, null);
  }

  /**
  * Method createAccountType
  * @param item item
  * @return Full HTTP response as Observable
  */
  public createAccountType(item: AccountType): Observable<HttpResponse<any>> {
    let uri = `/accountTypes`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<AccountType>('post', uri, headers, params, JSON.stringify(item));
  }

  /**
  * Method findAllAccounts
  * @return Full HTTP response as Observable
  */
  public findAllAccounts(): Observable<Account[]> {
    let uri = `/accounts`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    //return this.sendRequest<Account[]>('get', uri, headers, params, null);
    return this.http.get<Account[]>(this.domain +  uri);
  }

  public findAllCalendars(): Observable<Calendar[]> {
    let uri = `/calendars`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    //return this.sendRequest<Account[]>('get', uri, headers, params, null);
    return this.http.get<Calendar[]>(this.domain +  uri);
  }

/**
  * Method saveUsingPOST
  * @param account account
  * @return Full HTTP response as Observable
  */
  public saveAccount(account: Account): Observable<HttpResponse<any>> {
    let uri = `/accounts`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<any>('post', uri, headers, params, JSON.stringify(account));
  }

  /**
  * Method getCalculatedPropertiesUsingPOST
  * @param prototype prototype
  * @return Full HTTP response as Observable
  */
  public getCalculatedProperties(prototype: Account): Observable<HttpResponse<Account>> {
    let uri = `/accounts/calculateProperties`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<Account>('post', uri, headers, params, JSON.stringify(prototype));
  }

  /**
  * Method getSchedulesUsingPOST
  * @param prototype prototype
  * @return Full HTTP response as Observable
  */
  public getSolvedInstalments(prototype: Account): Observable<HttpResponse<Account>> {
    let uri = `/accounts/solveInstalments`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<Account>('post', uri, headers, params, JSON.stringify(prototype));
  }

  /**
  * Method createUsingGET
  * @param accountTypeName accountTypeName
  * @return Full HTTP response as Observable
  */
  public createAccount(accountTypeName: string): Observable<HttpResponse<Account>> {
    let uri = `/accounts/${accountTypeName}/new`;
    let headers = new HttpHeaders();
    let params = new HttpParams();


    return this.sendRequest<Account>('get', uri, headers, params, null);
  }

  /**
  * Method findAccountByAccountNumber
  * @param id id
  * @return Full HTTP response as Observable
  */
  public findAccountByAccountNumber(id: string): Observable<HttpResponse<Account>> {
    let uri = `/accounts/${id}`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
    return this.sendRequest<Account>('get', uri, headers, params, null);
  }


  /**
  * Method findAccountByAccountNumber
  * @param id id
  * @return Full HTTP response as Observable
  */
  public getTransactions(accountNumber: string, fromDate:string, toDate:string, positionType:string): Observable<HttpResponse<Transaction[]>> {
    let uri = `/accounts/${accountNumber}/transactions`;
    let headers = new HttpHeaders();
    let params = new HttpParams();
//&positionType1=Principal
    params.set('from', fromDate);
    params.set('to', toDate);
    params.set('positionType1', positionType);

    return this.sendRequest<Transaction[]>('get', uri, headers, params, null);
    //return this.http.get<Transaction[]>(this.domain +  uri,{ headers: headers.set('Accept', 'application/json'), params: params, observe: 'response' });
  }

  private sendRequest<T>(method: string, uri: string, headers: HttpHeaders, params: HttpParams, body: any): Observable<HttpResponse<T>> {
    if (method === 'get') {
      return this.http.get<T>(this.domain + uri, { headers: headers.set('Accept', 'application/json'), params: params, observe: 'response' });
    } else if (method === 'put') {
      return this.http.put<T>(this.domain + uri, body, { headers: headers.set('Content-Type', 'application/json'), params: params, observe: 'response' });
    } else if (method === 'post') {
      return this.http.post<T>(this.domain + uri, body, { headers: headers.set('Content-Type', 'application/json'), params: params, observe: 'response' });
    } else if (method === 'delete') {
      return this.http.delete<T>(this.domain + uri, { headers: headers, params: params, observe: 'response' });
    } else {
      console.error('Unsupported request: ' + method);
      return Observable.throw('Unsupported request: ' + method);
    }
  }
}
