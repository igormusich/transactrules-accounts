import { Component, OnInit } from '@angular/core';
import { ROUTE_TRANSITION } from '../../../../app.animation';
import { AccountCreateService } from '../../../../account-create.service'
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApiClientService } from 'app/api-client-service';
import forEach from 'lodash-es/forEach';
import { AmountValue, DateValue, OptionValue, Account, AccountType } from 'app/models';
import { toIsoString } from 'app/core/utils/format-date'
import { RateValue } from 'app/models/ratevalue.model';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/observable/from';
import 'rxjs/add/operator/toArray';
import { toDate }  from 'app/core/utils/format-date';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'vr-edit-account-details',
  templateUrl: './edit-account-details.component.html',
  styleUrls: ['./edit-account-details.component.scss']
})
export class EditAccountDetailsComponent implements OnInit {

  details_form: FormGroup;
  accountType: AccountType;
  account: Account;
  calendarName: String;

  constructor(public accountCreateService: AccountCreateService,
    private fb: FormBuilder,
    public apiClient: ApiClientService,
    public router: Router,
    private snackBar: MatSnackBar  ) { }

  ngOnInit() {
    this.accountType = this.accountCreateService.getAccountType();
    this.account = this.accountCreateService.getAccount();
    this.calendarName = this.accountCreateService.getCalendarName();
    this.details_form = this.toDetailsFormGroup(this.accountType, this.account);
  }

  toDetailsFormGroup(accountType: AccountType, account:Account): FormGroup {
    let group: any = {};

    accountType.dateTypes.forEach(element => {
      group[element.propertyName] =  new FormControl(this.getDate(element.propertyName),  Validators.required);
    });

    accountType.amountTypes.forEach(element => {
      group[element.propertyName] = new FormControl(this.getAmount(element.propertyName),  Validators.required);
    });

    accountType.rateTypes.forEach(element => {
      group[element.propertyName] = new FormControl(this.getRate(element.propertyName), Validators.required);
    });

    accountType.optionTypes.forEach(element => {
      group[element.propertyName] =new FormControl(this.getOption(element.propertyName) , Validators.required);
    });

    return new FormGroup(group);
  }

  onDetailsNextStep(event: any) {

    if(this.details_form.invalid){
      this.details_form.updateValueAndValidity();
      return;
    }

    this.mapFormToAccount();

    this.apiClient.getCalculatedProperties(this.account).subscribe( result => {
      this.account= result.body;
      this.accountCreateService.setAccount(this.account);
      this.router.navigate(['/data/create-account/schedules']);
    }, error =>{
      var errorMessage:string= "Account properties can't be calculated";

      if(error.status = 422){
        if(error.error.globalErrors != null && error.error.globalErrors.length>0 ){
          errorMessage = error.error.globalErrors[0].message;
        }
        else {
          var fieldErrors = '';
          error.error.fieldErrors.forEach(fieldError=> {
              fieldErrors = fieldErrors + fieldError.message + "\n";
          });

          errorMessage = fieldErrors;
        }
      }

      this.snackBar.open(errorMessage, null, {duration:3000});
      
    });
  }

  mapFormToAccount() {
    const formData = this.details_form.value;

    let keyArr: any[] = Object.keys(formData);

    keyArr.forEach((key: string) => {
      var value = formData[key];

      if(this.hasDateType(key)){
        var dateString = toIsoString(value);
        this.setDate(key,dateString);
      }

      if(this.hasAmountType(key)){
        this.setAmount(key, value);
      }

      if(this.hasRateType(key)){
        this.setRate(key,value);
      }

      if(this.hasOptionType(key)){
        this.setOption(key, value);
      }
    });
  }

  hasAmountType(key:string): boolean{
    var type = this.accountType.amountTypes.find(x => x.propertyName == key);

    return (type != null);
  }

  hasDateType(key:string){
    var type = this.accountType.dateTypes.find(x => x.propertyName == key);

    return (type != null);
  }

  hasRateType(key:string){
    var type = this.accountType.rateTypes.find(x => x.propertyName == key);

    return (type != null);
  }

  hasOptionType(key:string){
    var type = this.accountType.optionTypes.find(x => x.propertyName == key);

    return (type != null);
  }

  setAmount(key:string, value:number){
    var amountValue:AmountValue = new AmountValue();
    amountValue.amount = value;

    this.account.amounts[key] = amountValue;
  }

  getAmount(key:string):number {
    var value:number = 0;

    try {
      value = this.account.amounts[key].amount;
    }
    catch(e){}

    return value;
  }

  setDate(key:string, value:string){
    var dateValue:DateValue = new DateValue();
    dateValue.date = value;

    this.account.dates[key] = dateValue;
  }

  getDate(key:string):Date {
    var dateString:string = '';

    try {
      dateString = this.account.dates[key].data;

    }
    catch(e){}

    var value =toDate(dateString);
    return value;
  }

  setRate(key:string,value:number){
    var rateValue:RateValue = new RateValue();
    rateValue.value = value/100;

    this.account.rates[key] = rateValue;
  }

  getRate(key:string):number {
    var value:number = 0;

    try {
      value = this.account.rates[key].value*100;
    }
    catch(e){}

    return value;
  }

  setOption(key:string,value:string){
    var optionValue:OptionValue = this.account.options[key];

    optionValue.value = value;
  }

  getOption(key:string):string {
    var value:string = '';

    try {
      value = this.account.options[key].value;
    }
    catch(e){}

    return value;
  }

}
