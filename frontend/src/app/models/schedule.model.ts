import { ScheduleDate } from './scheduledate.model';

export class Schedule {
  businessDayCalculation: string;
  endDate: string;
  endType: string;
  frequency: string;
  interval: number;
  numberOfRepeats: number;
  startDate: string;
  excludeDates: ScheduleDate[];
  includeDates: ScheduleDate[];

  from(value:Schedule){
    this.businessDayCalculation = value.businessDayCalculation;
    this.endType = value.endDate;
    this.endType = value.endType;
    this.frequency = value.frequency;
    this.interval = value.interval;
    this.numberOfRepeats = value.numberOfRepeats;
    this.startDate = value.startDate;
    this.excludeDates = value.excludeDates;
    this.includeDates = value.includeDates;
  }
}
