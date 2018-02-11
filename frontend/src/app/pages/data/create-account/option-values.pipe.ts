import { Pipe, PipeTransform } from '@angular/core';
import { OptionValue } from 'app/models'

@Pipe({
  name: 'optionValues'
})
export class OptionValuesPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    var options:Map<string,OptionValue> = value;
    var optionTypeName:string = args;

    var values:string[] =  options[args].values;

    return values;
  }

}
