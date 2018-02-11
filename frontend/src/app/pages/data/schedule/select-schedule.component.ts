import { Component, OnInit, Input, OnChanges, DoCheck, 
  AfterContentChecked, AfterContentInit , AfterViewChecked , AfterViewInit, OnDestroy, forwardRef } from '@angular/core';
import { MatDialog } from '@angular/material';
import { Schedule } from 'app/models';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiClientService } from 'app/api-client-service';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { SimpleChanges } from '@angular/core';
import { toDate, toIsoString }  from 'app/core/utils/format-date';

@Component({
  selector: 'vr-select-schedule',
  templateUrl: './select-schedule.component.html',
  styleUrls: ['./select-schedule.component.scss'],
  providers: [
    { 
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => SelectScheduleComponent),
    }
  ]
})
export class SelectScheduleComponent implements OnChanges,
OnInit, ControlValueAccessor {
  

  @Input() schedule:Schedule;
  @Input() placeholder:String;

  form: FormGroup;

  // Function to call when the schedule changes.
  onChange = (schedule: Schedule) => {
    console.log(`onChange - new value is ${schedule}`);
    this.schedule = schedule;
  };

  // Function to call when the input is touched 
  onTouched = () => {};

  constructor(private apiService: ApiClientService,
    private fb: FormBuilder) { }

    toFormGroup(schedule:Schedule): FormGroup{
      var form = this.fb.group({
        startDate: new FormControl (toDate(schedule.startDate) , Validators.required ), 
        endType: new FormControl (schedule.endType , Validators.required ),
        endDate: new FormControl (toDate(schedule.endDate)),
        frequency: new FormControl (schedule.frequency, Validators.required ), 
        interval: new FormControl (schedule.interval , Validators.required ), 
        numberOfRepeats: new FormControl (schedule.numberOfRepeats),
        businessDayCalculation: new FormControl(schedule.businessDayCalculation),
        includeDates: new FormControl(schedule.includeDates),
        excludeDates: new FormControl(schedule.excludeDates)
      });

      return form;
    }


    writeValue(obj: Schedule): void {
      this.schedule = obj;

      if(this.form == null){
          this.form = this.toFormGroup(this.schedule);
      }
      else{
        this.form.setValue(this.schedule);
      } 
    }
  
     // Save the function as a property to call later here.
     registerOnChange(fn: (schedule: Schedule) => void): void {
      this.onChange = fn;
    }
  
    // Allows Angular to register a function to call when the input has been touched.
    // Save the function as a property to call later here.
    registerOnTouched(fn: () => void): void {
      this.onTouched = fn;
    }
  
    setDisabledState?(isDisabled: boolean): void {
      //console.log("setDisabledState");
    }
  


  ngOnInit() {
    //console.log(`ngOnInit  - schedule is ${this.schedule}`);
  }

  ngOnChanges(changes: SimpleChanges) {
    //console.log(`ngOnChanges - data is ${this.schedule}`);

    // for (let key in changes) {
    //   if(key!="placeholder"){
    //     console.log(`${key} changed. 
    //     Current: ${changes[key].currentValue}. 
    //     Previous: ${changes[key].previousValue}`);
    //   }
    // }

    if(this.form != null && this.schedule !=null){
      //console.log('ngOnChanges - setting form value to ${this.schedule}')
      this.form.setValue(this.schedule);
    }
  }

  parseForm(schedule:any){

    this.schedule.endType = this.form.value.endType;
    this.schedule.startDate = toIsoString(this.form.value.startDate);
    this.schedule.endDate = toIsoString(this.form.value.endDate)
    this.schedule.frequency = this.form.value.frequency;
    this.schedule.interval = this.form.value.interval;
    this.schedule.numberOfRepeats = this.form.value.numberOfRepeats;
    this.schedule.includeDates = this.form.value.includeDates;
    this.schedule.excludeDates = this.form.value.excludeDates;
    this.schedule.businessDayCalculation = this.form.value.businessDayCalculation;

    if(this.form.valid){
      //this.selected.sub_organism_id = null;
    };  

    console.log(`parseForm  ${JSON.stringify(this.schedule)}`);
  }

  parseStartDate(date:Date){
    this.schedule.startDate = toIsoString(date);
  }

  parseEndDate(date:Date){
    this.schedule.endDate = toIsoString(date);
  }

}
