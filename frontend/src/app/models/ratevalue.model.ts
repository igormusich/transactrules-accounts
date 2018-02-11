


export class RateValue {
  value: number;
  valueDate: string;

  from(value:RateValue){
    this.value = value.value;
    this.valueDate = value.valueDate;
  }
}
