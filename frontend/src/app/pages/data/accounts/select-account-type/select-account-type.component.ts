import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { ApiClientService } from 'app/api-client-service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { FieldError } from 'app/errorModel/fielderror.model';
import { AccountType } from 'app/models/accounttype.model';
import { CalendarComponent } from 'app/pages/data/calendar/calendar.component';
import { Calendar } from 'app/models/calendar.model';
import { forkJoin } from 'rxjs/observable/forkJoin';
//import { read } from 'fs';

@Component({
  selector: 'vr-select-account-type',
  templateUrl: './select-account-type.component.html',
  styleUrls: ['./select-account-type.component.scss']
})
export class SelectAccountTypeComponent implements OnInit {

  form: FormGroup;
  accountTypes: AccountType[];
  calendars: Calendar[];

  constructor(private dialogRef: MatDialogRef<SelectAccountTypeComponent>,
    private apiService: ApiClientService,
    private fb: FormBuilder) { }

  ngOnInit() {

    this.form = this.fb.group({
      accountTypeName: new FormControl ('', Validators.required ),
      calendarName: new FormControl ('', Validators.required)
    });

    this.apiService.findAllAccountTypes().subscribe(accountTypes=> {
      this.accountTypes = accountTypes;
    });

    this.apiService.findAllCalendars().subscribe(calendars =>{
      this.calendars = calendars;
    });
  }

  create(){

    const request = this.form.value;

    if(this.form.invalid){
      return;
    }

    let accountType = this.apiService.getAccountType(request.accountTypeName);
    let account = this.apiService.createAccount(request.accountTypeName);

    forkJoin([accountType, account]).subscribe(results => {
      // results[0] is accountType
      // results[1] is account
      this.dialogRef.close( {
        'message':'new Account created',
        'accountType': results[0].body,
        'account': results[1].body,
        'calendarName': request.calendarName
      } );
    });

  }

}

