import { Injectable } from '@angular/core';
import { AccountType,Account } from 'app/models';

@Injectable()
export class AccountCreateService {

  constructor() { }

  private accountType:AccountType;
  private calendarName:String;
  private account:Account;

  setAccount(account:Account){
      this.account = account;
  }

  getAccount(){
      return this.account;
  }

  setAccountType(accountType:AccountType){
      this.accountType = accountType;
  }

  getAccountType():AccountType{
      return this.accountType;
  }

  setCalendarName(calendarName:String){
      this.calendarName = calendarName;
  }

  getCalendarName(){
      return this.calendarName;
  }
}
