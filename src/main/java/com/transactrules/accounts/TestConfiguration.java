package com.transactrules.accounts;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.repository.AccountTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TestConfiguration {

    @Autowired
    private AccountTypeRepository accountTypeRepo;


    public void run() throws Exception {
        Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

        logger.info("Saving default metadata (SavingsAccount)");

        AccountType savingsAccountType = createSavingsAccountType();

        accountTypeRepo.save(savingsAccountType);

        logger.info("Saved account type:" + savingsAccountType.getClassName().toString());

        AccountType loanAccountType = createLoanGivenAccountType();

        accountTypeRepo.save(loanAccountType);

        logger.info("Saved account type:" + loanAccountType.getClassName().toString());

        logger.info("Default metadata saved (SavingsAccount,LoanGiven)");

        logger.info("press any key ...");
    }

    public static AccountType createSavingsAccountType()
    {
        AccountType accountType = new AccountType( "SavingsAccount", "Savings Account");

        PositionType currentPosition = accountType.addPositionType( "Current");
        PositionType interestAccruedPosition = accountType.addPositionType( "InterestAccrued" );

        TransactionType depositTransaction = accountType.addTransactionType("Deposit", false);

        depositTransaction.addRule(currentPosition, TransactionOperation.Add);

        TransactionType withdrawalTransaction = accountType.addTransactionType("Withdrawal",false);

        withdrawalTransaction.addRule(currentPosition, TransactionOperation.Subtract);

        TransactionType interestAccruedTransaction = accountType.addTransactionType("InterestAccrued", true);

        interestAccruedTransaction.addRule(interestAccruedPosition, TransactionOperation.Add);

        TransactionType interestCapitalizedTransaction = accountType.addTransactionType("InterestCapitalized", false);

        interestCapitalizedTransaction.addRule(interestAccruedPosition, TransactionOperation.Subtract );
        interestCapitalizedTransaction.addRule(currentPosition, TransactionOperation.Add );

        return accountType;
    }

    public static AccountType createLoanGivenAccountType()
    {
        AccountType loanGiven = new AccountType("LoanGiven", "LoanGiven");

        PositionType conversionInterestPosition = loanGiven.addPositionType("ConversionInterest");
        PositionType earlyRedemptionFeePosition = loanGiven.addPositionType("EarlyRedemptionFee");
        PositionType interestAccruedPosition = loanGiven.addPositionType("InterestAccrued");
        PositionType interestCapitalizedPosition = loanGiven.addPositionType("InterestCapitalized");
        PositionType principalPosition = loanGiven.addPositionType( "Principal" );

        DateType startDate = loanGiven.addDateType( "StartDate" , true, true);
        DateType accrualStart = loanGiven.addDateType( "AccrualStart" , true,false);
        DateType endDate = loanGiven.addDateType( "EndDate" ,true, false);


        ScheduleType accrualSchedule = loanGiven.addCalculatedScheduleType(
                "AccrualSchedule",
                ScheduleFrequency.Daily,
                ScheduleEndType.NoEnd,
                BusinessDayCalculation.AnyDay,
                "this.StartDate()",
                "",
                "",
                "1"
        );

        ScheduleType interestSchedule =loanGiven.addUserInputScheduleType(
                "InterestSchedule",
                ScheduleFrequency.Monthly,
                ScheduleEndType.EndDate,
                BusinessDayCalculation.AnyDay,
                null,
                null,
                null,
                "1");

        ScheduleType redemptionSchedule = loanGiven.addUserInputScheduleType(
                "RedemptionSchedule",
                ScheduleFrequency.Monthly,
                ScheduleEndType.EndDate,
                BusinessDayCalculation.AnyDay,
                null,
                null,
                null,
                "1"
        );

        TransactionType interestAccrued=  loanGiven.addTransactionType("InterestAccrued", true)
                .addRule(interestAccruedPosition, TransactionOperation.Add);

        TransactionType interestCapitalized= loanGiven.addTransactionType("InterestCapitalized")
                .addRule(principalPosition,  TransactionOperation.Add )
                .addRule(interestAccruedPosition,  TransactionOperation.Subtract )
                .addRule(interestCapitalizedPosition, TransactionOperation.Add );


        loanGiven.addTransactionType("Redemption")
                .addRule(principalPosition, TransactionOperation.Subtract );

        TransactionType advanceTransactionType = loanGiven.addTransactionType("Advance")
                .addRule( principalPosition,  TransactionOperation.Add );

        loanGiven.addTransactionType("AdditionalAdvance")
                .addRule(principalPosition, TransactionOperation.Add);

        loanGiven.addTransactionType("ConversionInterest")
                .addRule(conversionInterestPosition, TransactionOperation.Add);

        loanGiven.addTransactionType("EarlyRedemptionFee")
                .addRule(earlyRedemptionFeePosition, TransactionOperation.Add);

        loanGiven.addTransactionType("FXResultInterest")
                .addRule(interestAccruedPosition, TransactionOperation.Add);

        loanGiven.addTransactionType("FXResultPrincipal")
                .addRule(principalPosition, TransactionOperation.Add);

        loanGiven.addTransactionType("InterestPayment")
                .addRule(interestAccruedPosition, TransactionOperation.Subtract);

        loanGiven.addAmountType("RedemptionAmount" , false, false);
        loanGiven.addAmountType("AdditionalAdvanceAmount" , false, false);
        loanGiven.addAmountType("ConversionInterestAmount" , false, false);
        loanGiven.addAmountType("AdvanceAmount" , false, true);

        loanGiven.addRateType("InterestRate");

        loanGiven.addOptionType(
                "AccrualOption",
                "com.transactrules.accounts.calculations.AccrualCalculation.AccrualOptions()");


        loanGiven.addDayScheduledTransaction(
                "Advance",
                ScheduledTransactionTiming.StartOfDay,
                startDate,
                advanceTransactionType,
                "AdvanceAmount()",
                1);

        loanGiven.addScheduledTransaction(
                "InterestAccrual",
                ScheduledTransactionTiming.EndOfDay,
                accrualSchedule,
                interestAccrued,
                "com.transactrules.accounts.calculations.AccrualCalculation.InterestAccrued(AccrualOption(), Principal(), InterestRate(), ValueDate())" ,
                1 );

        loanGiven.addScheduledTransaction(
                "InterestCapitalized",
                ScheduledTransactionTiming.EndOfDay,
                interestSchedule,
                interestCapitalized,
                "InterestAccrued()",
                2);

        loanGiven.addInstalmentType(
                "Redemptions",
                ScheduledTransactionTiming.StartOfDay,
                redemptionSchedule.propertyName,
                "Redemption","Principal",
                "InterestAccrued",
                "InterestCapitalized" );

        return loanGiven;
    }

}
