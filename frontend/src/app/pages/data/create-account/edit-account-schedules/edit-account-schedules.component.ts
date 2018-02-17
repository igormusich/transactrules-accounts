import { Component, OnInit } from '@angular/core';
import { AccountCreateService } from '../../../../account-create.service'
import { ApiClientService } from 'app/api-client-service';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountType } from 'app/models/accounttype.model';
import { Schedule } from 'app/models/schedule.model';
import { ScheduleControl } from 'app/pages/data/schedule/schedule-control';
import { Account } from 'app/models/account.model';
import { ControlValueAccessor } from '@angular/forms/src/directives/control_value_accessor';
import { toIsoString } from 'app/core/utils/format-date';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'vr-edit-account-schedules',
  templateUrl: './edit-account-schedules.component.html',
  styleUrls: ['./edit-account-schedules.component.scss']
})
export class EditAccountSchedulesComponent implements OnInit {

  accountType: AccountType;
  account: Account;
  schedules:Array<ScheduleControl>;
  schedules_form: FormGroup;

  constructor(public accountCreateService: AccountCreateService,
    private fb: FormBuilder,
    public apiClient: ApiClientService,
    public router: Router,
    private snackBar: MatSnackBar ) { }

  ngOnInit() {
    this.accountType = this.accountCreateService.getAccountType();
    this.account = this.accountCreateService.getAccount();
    this.schedules = this.createScheduleControls(this.accountType, this.account);
    this.schedules_form = this.toScheduleFormGroup(this.accountType, this.account);
  }

  toScheduleFormGroup(accountType: AccountType, account:Account): FormGroup {
    let group: any = {};

    accountType.scheduleTypes.forEach(element => {
      var schedule:Schedule = account.schedules[element.propertyName];
      group[element.propertyName] = new FormControl(schedule, Validators.required);
    });

    return new FormGroup(group);
  }

  createScheduleControls(accountType:AccountType, account:Account):Array<ScheduleControl>{
    var controls = new Array<ScheduleControl>();

    accountType.scheduleTypes.forEach(scheduleType=>{
      var schedule:Schedule = account.schedules[scheduleType.propertyName];
      var control = new ScheduleControl();
      control.scheduleType = scheduleType;
      control.schedule = schedule;

      controls.push(control);
    });

    return controls;
  }

  onPreviousStep(){
    this.mapFormToAccount();
    this.router.navigate(['/data/create-account']);
  }

  onNextStep(){
    this.mapFormToAccount();

    this.apiClient.getSolvedInstalments(this.account).subscribe( result => {
      this.account= result.body;
      this.accountCreateService.setAccount(this.account);
      this.router.navigate(['/data/create-account/instalments']);
    }, error => {
      var errorMessage:string= "Instalments can't be calculated";

      if(error.status = 422){
        if(error.error.globalErrors != null && error.error.globalErrors.length>0 ){
          errorMessage = error.error.globalErrors[0].message;
        }
      }

      this.snackBar.open(errorMessage, null, {duration:3000});
    });
  }

  mapFormToAccount() {
    const formData = this.schedules_form.value;

    let keyArr: any[] = Object.keys(formData);

    keyArr.forEach((key: string) => {
      var value:Schedule = formData[key];

      this.account.schedules[key] = this.getScheduleData(value);

    });
  }

  private getScheduleData(formValues:any):Schedule{
    var schedule = new Schedule();

    schedule.businessDayCalculation = formValues.businessDayCalculation;
    schedule.endDate = formValues.endDate;
    schedule.endType = formValues.endType;
    schedule.excludeDates = formValues.excludeDates;
    schedule.frequency = formValues.frequency;
    schedule.includeDates = formValues.includeDates;
    schedule.interval = formValues.interval;
    schedule.numberOfRepeats = formValues.numberOfRepeats;
    schedule.startDate = formValues.startDate;

    return schedule;
  }


}
