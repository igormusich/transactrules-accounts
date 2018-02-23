package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.runtime.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtility {

    public static Account CreateLoanGivenAccount(String accountNumber, LocalDate startDate, LocalDate endDate, CodeGenService codeGenService) {

        AccountType accountType = TestConfiguration.createLoanGivenAccountType();
        Class accountClass = codeGenService.getAccountClass(accountType);

        AccountBuilder builder = new AccountBuilder( accountType.getClassName(), accountNumber, accountClass );

        Calendar calendar= CreateEuroZoneCalendar();

        builder.setBusinessDayCalculator(calendar)
                .addDateValue("StartDate", startDate)
                .addDateValue("AccrualStart",startDate)
                .addDateValue("EndDate", endDate)
                .addAmountValue("AdvanceAmount", BigDecimal.valueOf(624000), startDate)
                .addRateValue("InterestRate", BigDecimal.valueOf(3.04/100), startDate)
                .addOptionValue("AccrualOption", "365")
                .addCalendar(calendar.getName())
        ;


        Account account =  builder.getAccount();

        account.setCalculated();

        return account;
    }

    public static Account CreateLoanGivenAccountWithSchedules(String accountNumber, LocalDate startDate, LocalDate endDate, CodeGenService codeGenService) {

        AccountType accountType = TestConfiguration.createLoanGivenAccountType();
        Class accountClass = codeGenService.getAccountClass(accountType);

        AccountBuilder builder = new AccountBuilder( accountType.getClassName(), accountNumber, accountClass );
        BusinessDayCalculator calendar = CreateEuroZoneCalendar();


        builder.setBusinessDayCalculator(calendar)
                .addDateValue("StartDate", startDate)
                .addDateValue("AccrualStart",startDate)
                .addDateValue("EndDate", endDate)
                .addAmountValue("AdvanceAmount", BigDecimal.valueOf(624000), startDate)
                .addRateValue("InterestRate", BigDecimal.valueOf(3.04/100), startDate)
                .addOptionValue("AccrualOption", "365")
        ;


        Account account =  builder.getAccount();

        account.setCalculated();

        //Schedule accrualSchedule = account.getSchedules().get("AccrualSchedule");
        Schedule interestSchedule = account.getSchedules().get("InterestSchedule");
        Schedule redemptionSchedule = account.getSchedules().get("RedemptionSchedule");

        LocalDate interestStart = LocalDate.of (2013, 3, 31);

        interestSchedule.setStartDate(interestStart);
        interestSchedule.setEndDate(endDate);
        interestSchedule.getIncludeDates().add(new ScheduleDate(endDate) );

        redemptionSchedule.setStartDate(interestStart);
        redemptionSchedule.setEndDate(endDate);
        redemptionSchedule.getIncludeDates().add(new ScheduleDate (endDate) );

        return account;
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
