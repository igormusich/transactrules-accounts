import { HolidayDate } from './holidaydate.model';

export interface Calendar {
  default: boolean;
  name: string;
  holidays: HolidayDate[];
}
