import { AmountValue } from "app/models/amountvalue.model";
import { DateValue } from "app/models/datevalue.model";
import { InstalmentSet } from "app/models/instalmentset.model";
import { OptionValue } from "app/models/optionvalue.model";
import { RateValue } from "app/models/ratevalue.model";
import { Schedule } from "app/models/schedule.model";
import { Transaction } from "app/models/transaction.model";
import { Position } from "app/models/position.model";

export class Account {
  accountNumber: string;
  accountTypeName: string;
  active: boolean;
  dateActivated: string;
  amounts: Map<string,AmountValue> = new Map<string,AmountValue>() ;
  calendarNames: string[];
  dates: Map<string,DateValue> = new Map<string,DateValue>() ;
  instalmentSets: Map<string,InstalmentSet> = new Map<string,InstalmentSet>();
  options: Map<string, OptionValue> = new Map<string, OptionValue>();
  positions: Map<string,Position> = new Map<string,Position>() ;
  rates: Map<string,RateValue> = new  Map<string,RateValue>() ;
  schedules: Map<string,Schedule> = new Map<string,Schedule>() ;
  transactions: Transaction[];

  from(value:Account){
    this.accountNumber = value.accountNumber;
    this.accountTypeName = value.accountTypeName;
    this.active = value.active;
    this.calendarNames = value.calendarNames;

    var keys = Object.keys(value.amounts);

    keys.forEach((key:string)=>{
      var amount:AmountValue = new AmountValue();
      amount.from(value.amounts[key]);
      this.amounts.set(key, amount);
    });

    var keys = Object.keys(value.dates);

    keys.forEach((key:string)=>{
      var date = new DateValue();
      date.from(value.dates[key])
      this.dates[key]= date;
    });

    var keys = Object.keys(value.instalmentSets);

    keys.forEach((key:string)=>{
      var instlmentSet = new InstalmentSet();
      instlmentSet.from(value.instalmentSets[key]);
      this.instalmentSets[key]= InstalmentSet;
    });

    var keys = Object.keys(value.options);

    keys.forEach((key:string)=>{
      var option = new OptionValue();
      option.from(value.options[key]);
      this.options[key]= option;
    });

    var keys = Object.keys(value.positions);

    keys.forEach((key:string)=>{
      var position = new Position();
      position.from(value.positions[key]);
      this.positions[key]= position;
    });

    var keys = Object.keys(value.rates);

    keys.forEach((key:string)=>{
      var rate = new RateValue();
      rate.from(value.rates[key]);
      this.rates[key]= rate;
    });

    var keys = Object.keys(value.schedules);

    keys.forEach((key:string)=>{
      var schedule = new Schedule();
      schedule.from(value.schedules[key]);
      this.schedules[key]= schedule;
    });

  }
}
