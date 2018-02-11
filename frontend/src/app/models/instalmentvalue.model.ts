
export class InstalmentValue {
  amount: number;
  hasFixedValue: boolean;
  date: string;

  from(value:InstalmentValue, date:string){
    this.amount = value.amount;
    this.hasFixedValue = value.hasFixedValue;
    this.date = date;
  }
}
