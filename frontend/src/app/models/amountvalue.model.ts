
export class AmountValue {
  amount: number;
  valueDate: string;

  from(value:AmountValue){
    this.amount = value.amount;
    this.valueDate = value.valueDate;
  }
}
