package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.BusinessDayCalculation;
import com.transactrules.accounts.metadata.ScheduleEndType;
import com.transactrules.accounts.metadata.ScheduleFrequency;
import com.transactrules.accounts.runtime.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTest {

    @Autowired
    CodeGenService codeGenService;
    
   @Test
    public void TestSchedules()
    {
        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears (25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();

        Account account = CreateLoanGivenAccount(TestUtility.CreateLoanGivenAccountType(), startDate, endDate,calendar);


        Schedule accrualSchedule = new Schedule();
        accrualSchedule.businessDayCalculator = calendar;
        accrualSchedule.setBusinessDayCalculation(BusinessDayCalculation.AnyDay.value());
        accrualSchedule.setStartDate( startDate);
        accrualSchedule.setInterval(1);
        accrualSchedule.setFrequency(ScheduleFrequency.Daily.value());
        accrualSchedule.setEndType(ScheduleEndType.NoEnd.value());


        LocalDate interestStart =  LocalDate.of(2013, 3, 31);

        Schedule interestSchedule = new Schedule();
        interestSchedule.setBusinessDayCalculation( BusinessDayCalculation.AnyDay.value());
        interestSchedule.businessDayCalculator = calendar;
        interestSchedule.setFrequency(ScheduleFrequency.Monthly.value());
        interestSchedule.setEndType( ScheduleEndType.EndDate.value());
        interestSchedule.setInterval(1);
        interestSchedule.setStartDate( interestStart);
        interestSchedule.setEndDate(endDate);
        interestSchedule.getIncludeDates().add(new ScheduleDate( endDate ));

        List<LocalDate> accrualDates = accrualSchedule.GetAllDates(endDate);

        assertThat(startDate, is(accrualDates.stream().findFirst().get()));

        assertThat(endDate, is(accrualDates.stream().reduce((a, b) -> b).orElse(null)));

        assertThat( endDate.toEpochDay()- startDate.toEpochDay() +1, is( (long) accrualDates.size()));

        List<LocalDate> interestDates = interestSchedule.GetAllDates(endDate);

        assertThat(interestStart, is(interestDates.stream().findFirst().get()));
        assertThat(LocalDate.of(2013,4,30) , is(interestDates.stream().skip(1).findFirst().get()));
        assertThat(LocalDate.of(2013, 5, 31), is(interestDates.stream().skip(2).findFirst().get()));
        assertThat(LocalDate.of(2014, 2, 28), is(interestDates.stream().skip(11).findFirst().get()));
        assertThat(endDate, is(interestDates.stream().reduce((a, b) -> b).orElse(null)));
    }

    private  Account CreateLoanGivenAccount(AccountType accountType, LocalDate startDate, LocalDate endDate, BusinessDayCalculator businessDayCalculator) {
       Class accountClass = codeGenService.generateClass(accountType);

       AccountBuilder builder = new AccountBuilder(accountType.getClassName(),"10010", accountClass);

       builder.setBusinessDayCalculator(businessDayCalculator)
               .addDateValue("StartDate", startDate)
               .addDateValue("AccrualStart",startDate)
               .addDateValue("EndDate", endDate)
               .addAmountValue("AdvanceAmount",BigDecimal.valueOf(624000), startDate)
               .addRateValue("InterestRate", BigDecimal.valueOf(3.04/100), startDate)
               .addOptionValue("AccrualOption", "365");

        return builder.getAccount();
    }
}
