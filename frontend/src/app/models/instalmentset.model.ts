import { InstalmentValue } from "app/models/instalmentvalue.model";

export class InstalmentSet {
  instalments: Map<string,InstalmentValue>;

  from(value:InstalmentSet){
    var keys = Object.keys(value.instalments);
    
    keys.forEach((key:string)=>{
      var instalmentvalue: InstalmentValue = new InstalmentValue();
      instalmentvalue.from(value.instalments[key], key);
      this.instalments[key] = instalmentvalue;
    });

  }
}
