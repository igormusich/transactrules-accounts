import { AmountType } from './amounttype.model';
import { DateType } from './datetype.model';
import { InstalmentType } from './instalmenttype.model';
import { OptionType } from './optiontype.model';
import { PositionType } from './positiontype.model';
import { RateType } from './ratetype.model';
import { ScheduleType } from './scheduletype.model';
import { ScheduledTransaction } from './scheduledtransaction.model';
import { TransactionType } from './transactiontype.model';

export class AccountType {
  className: string;
  labelName: string;
  amountTypes: AmountType[];
  dateTypes: DateType[];
  instalmentTypes: InstalmentType[];
  optionTypes: OptionType[];
  positionTypes: PositionType[];
  rateTypes: RateType[];
  scheduleTypes: ScheduleType[];
  scheduledTransactions: ScheduledTransaction[];
  transactionTypes: TransactionType[];

}
