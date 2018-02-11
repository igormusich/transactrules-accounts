import { TransactionRuleType } from './transactionruletype.model';

export interface TransactionType {
  labelName: string;
  maximumPrecision: boolean;
  propertyName: string;
  transactionRules: TransactionRuleType[];
}
