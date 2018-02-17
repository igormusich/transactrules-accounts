
export class InstalmentValue {
  amount: number;
  hasFixedValue: boolean;
  data: string;

  from(value:InstalmentValue, data:string){
    this.amount = value.amount;
    this.hasFixedValue = value.hasFixedValue;
    this.data = data;
  }
}
