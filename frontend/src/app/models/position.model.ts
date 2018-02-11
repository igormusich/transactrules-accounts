
export class Position {
  amount: number;

  from(value:Position){
    this.amount = value.amount;
  }
}
