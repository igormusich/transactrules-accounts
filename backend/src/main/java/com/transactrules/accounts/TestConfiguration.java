package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.*;
import com.transactrules.accounts.metadata.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.repository.CalendarRepository;
import com.transactrules.accounts.runtime.repository.SystemPropertiesRepository;
import com.transactrules.accounts.runtime.repository.UniqueIdRepository;
import com.transactrules.accounts.runtime.domain.Calendar;
import com.transactrules.accounts.runtime.domain.SystemProperties;
import com.transactrules.accounts.runtime.domain.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class TestConfiguration {

    @Autowired
    private AccountTypeRepository accountTypeRepo;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    SystemPropertiesRepository systemPropertiesRepository;

    @Autowired
    UniqueIdRepository uniqueIdRepository;


    public void run() throws Exception {


        Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

        UniqueId accountId = new UniqueId("Account", 201L,"ACC-002-" ,7);

        uniqueIdRepository.save(accountId);

        systemPropertiesRepository.save(new SystemProperties("default", LocalDate.now()));

        calendarRepository.save(CreateEuroZoneCalendar());


        logger.info("Saving default metadata (SavingsAccount)");

        AccountType savingsAccountType = createSavingsAccountType();

        accountTypeRepo.save(savingsAccountType);

        logger.info("Saved account type:" + savingsAccountType.getClassName().toString());

        AccountType loanAccountType = createLoanGivenAccountType();

        accountTypeRepo.save(loanAccountType);

        logger.info("Saved account type:" + loanAccountType.getClassName().toString());

        AccountType serviceAccount = createServiceAccount();

        accountTypeRepo.save(loanAccountType);

        logger.info("Saved account type:" + serviceAccount.getClassName().toString());

        logger.info("Default metadata saved (SavingsAccount,LoanGiven, ServiceAccount)");

        logger.info("press any key ...");
    }

    public static AccountType createServiceAccount() {
        AccountType serviceAccount = new AccountType("ServiceAccount", "ServiceAccount");

        PositionType utilization = serviceAccount.addPositionType( "Utilization");
        PositionType paymentDue = serviceAccount.addPositionType( "PaymentDue");

        TransactionType activityTransaction =
                serviceAccount
                        .addTransactionType("Activity", true)
                        .addRule(utilization, TransactionOperation.Add);

        TransactionType activityCharged =
                serviceAccount
                        .addTransactionType("ActivityCharged", true)
                        .addRule(utilization, TransactionOperation.Subtract);

        TransactionType paymentDueTransaction =
                serviceAccount
                        .addTransactionType("PaymentDue", true)
                        .addRule(paymentDue, TransactionOperation.Add);

        TransactionType paymentBilledTransaction =
                serviceAccount
                        .addTransactionType("PaymentBilled", true)
                        .addRule(paymentDue, TransactionOperation.Subtract);

        DateType startDate =
                serviceAccount
                        .addDateType( "StartDate" , true, true);

        ScheduleType billingSchedule =
                serviceAccount
                        .addCalculatedScheduleType(
                      "BillingSchedule",
                            ScheduleFrequency.Monthly,
                            ScheduleEndType.NoEnd,
                            BusinessDayCalculation.AnyDay,
                            "this.StartDate()",
                            "",
                            "",
                            "1"
                    );

        return serviceAccount;
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
        AccountType loanGiven = new AccountType("Loan", "Loan");

        PositionType conversionInterestPosition = loanGiven.addPositionType("ConversionInterest");
        PositionType earlyRedemptionFeePosition = loanGiven.addPositionType("EarlyRedemptionFee");
        PositionType interestAccruedPosition = loanGiven.addPositionType("InterestAccrued");
        PositionType interestCapitalizedPosition = loanGiven.addPositionType("InterestCapitalized");
        PositionType principalPosition = loanGiven.addPositionType( "Principal" );

        principalPosition.setPrincipal(true);

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
                "StartDate()",
                "EndDate()",
                null,
                "1");

        interestSchedule.setIncludeDatesExpression("fromDates(EndDate())");

        ScheduleType redemptionSchedule = loanGiven.addUserInputScheduleType(
                "RedemptionSchedule",
                ScheduleFrequency.Monthly,
                ScheduleEndType.EndDate,
                BusinessDayCalculation.AnyDay,
                "StartDate().plusMonths(1)",
                "EndDate()",
                null,
                "1"
        );

        redemptionSchedule.setIncludeDatesExpression("fromDates(EndDate())");

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

        //loanGiven.addAmountType("RedemptionAmount" , false, false);
        //loanGiven.addAmountType("AdditionalAdvanceAmount" , false, false);
        //loanGiven.addAmountType("ConversionInterestAmount" , false, false);
        loanGiven.addAmountType("AdvanceAmount" , false, true);

        loanGiven.addRateType("InterestRate", true);

        loanGiven.addOptionType(
                "AccrualOption",
                "com.transactrules.accounts.calculations.AccrualCalculation.AccrualOptions()", true);


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

    public static Calendar CreateEuroZoneCalendar()
    {

        return new Calendar("Euro Zone",true)
                .add("GOOD FRIDAY", LocalDate.parse("2000-04-21"))
                .add("EASTER MONDAY", LocalDate.parse("2000-04-24"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2000-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2000-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2000-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2001-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2001-04-13"))
                .add("EASTER MONDAY", LocalDate.parse("2001-04-16"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2001-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2001-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2001-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2002-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2002-03-29"))
                .add("EASTER MONDAY", LocalDate.parse("2002-04-01"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2002-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2002-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2002-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2003-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2003-04-18"))
                .add("EASTER MONDAY", LocalDate.parse("2003-04-21"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2003-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2003-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2003-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2004-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2004-04-09"))
                .add("EASTER MONDAY", LocalDate.parse("2004-04-12"))
                .add("GOOD FRIDAY", LocalDate.parse("2005-03-25"))
                .add("EASTER MONDAY", LocalDate.parse("2005-03-28"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2005-12-26"))
                .add("GOOD FRIDAY", LocalDate.parse("2006-04-14"))
                .add("EASTER MONDAY", LocalDate.parse("2006-04-17"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2006-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2006-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2006-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2007-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2007-04-06"))
                .add("EASTER MONDAY", LocalDate.parse("2007-04-09"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2007-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2007-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2007-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2008-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2008-03-21"))
                .add("EASTER MONDAY", LocalDate.parse("2008-03-24"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2008-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2008-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2008-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2009-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2009-04-10"))
                .add("EASTER MONDAY", LocalDate.parse("2009-04-13"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2009-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2009-12-25"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2010-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2010-04-02"))
                .add("EASTER MONDAY", LocalDate.parse("2010-04-05"))
                .add("GOOD FRIDAY", LocalDate.parse("2011-04-22"))
                .add("EASTER MONDAY", LocalDate.parse("2011-04-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2011-12-26"))
                .add("GOOD FRIDAY", LocalDate.parse("2012-04-06"))
                .add("EASTER MONDAY", LocalDate.parse("2012-04-09"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2012-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2012-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2012-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2013-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2013-03-29"))
                .add("EASTER MONDAY", LocalDate.parse("2013-04-01"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2013-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2013-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2013-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2014-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2014-04-18"))
                .add("EASTER MONDAY", LocalDate.parse("2014-04-21"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2014-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2014-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2014-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2015-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2015-04-03"))
                .add("EASTER MONDAY", LocalDate.parse("2015-04-06"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2015-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2015-12-25"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2016-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2016-03-25"))
                .add("EASTER MONDAY", LocalDate.parse("2016-03-28"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2016-12-26"))
                .add("GOOD FRIDAY", LocalDate.parse("2017-04-14"))
                .add("EASTER MONDAY", LocalDate.parse("2017-04-17"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2017-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2017-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2017-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2018-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2018-03-30"))
                .add("EASTER MONDAY", LocalDate.parse("2018-04-02"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2018-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2018-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2018-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2019-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2019-04-19"))
                .add("EASTER MONDAY", LocalDate.parse("2019-04-22"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2019-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2019-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2019-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2020-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2020-04-10"))
                .add("EASTER MONDAY", LocalDate.parse("2020-04-13"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2020-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2020-12-25"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2021-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2021-04-02"))
                .add("EASTER MONDAY", LocalDate.parse("2021-04-05"))
                .add("GOOD FRIDAY", LocalDate.parse("2022-04-15"))
                .add("EASTER MONDAY", LocalDate.parse("2022-04-18"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2022-12-26"))
                .add("GOOD FRIDAY", LocalDate.parse("2023-04-07"))
                .add("EASTER MONDAY", LocalDate.parse("2023-04-10"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2023-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2023-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2023-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2024-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2024-03-29"))
                .add("EASTER MONDAY", LocalDate.parse("2024-04-01"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2024-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2024-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2024-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2025-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2025-04-18"))
                .add("EASTER MONDAY", LocalDate.parse("2025-04-21"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2025-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2025-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2025-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2026-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2026-04-03"))
                .add("EASTER MONDAY", LocalDate.parse("2026-04-06"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2026-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2026-12-25"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2027-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2027-03-26"))
                .add("EASTER MONDAY", LocalDate.parse("2027-03-29"))
                .add("GOOD FRIDAY", LocalDate.parse("2028-04-14"))
                .add("EASTER MONDAY", LocalDate.parse("2028-04-17"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2028-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2028-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2028-12-26"))
                .add("NEW YEARS DAY (01JAN)", LocalDate.parse("2029-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2029-03-30"))
                .add("EASTER MONDAY", LocalDate.parse("2029-04-02"))
                .add("LABOUR DAY (01 MAY)", LocalDate.parse("2029-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2029-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2029-12-26"))
                .add("NEW YEARS DAY (1 JAN)", LocalDate.parse("2030-01-01"))
                .add("GOOD FRIDAY", LocalDate.parse("2030-04-19"))
                .add("EASTER MONDAY", LocalDate.parse("2030-04-22"))
                .add("LABOUR DAY", LocalDate.parse("2030-05-01"))
                .add("CHRISTMAS DAY (25 DEC)", LocalDate.parse("2030-12-25"))
                .add("BOXING DAY (26 DEC)", LocalDate.parse("2030-12-26"));

    }

}
