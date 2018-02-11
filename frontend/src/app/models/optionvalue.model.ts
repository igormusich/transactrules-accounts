
export class OptionValue {
  value: string;
  values: string[];

  from(value:OptionValue){
    this.value = value.value;
    this.values = value.values;
  }
}
