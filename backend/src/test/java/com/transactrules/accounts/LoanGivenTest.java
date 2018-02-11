package com.transactrules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.*;
import com.transactrules.accounts.services.AccountTypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanGivenTest {

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    AccountTypeService accountTypeService;


    AccountType loanGivenAccountType;

    Class loanGivenClass;

    Account loanGivenAccount;

    @Before
    public void Setup(){
        loanGivenAccountType = TestUtility.CreateLoanGivenAccountType();
        loanGivenAccountType.setClassName("LoanGivenTestAccount");

        accountTypeService.save(loanGivenAccountType);
    }

    @Test
    public void TestTransactions()
    {
        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears(25);

        Account account = TestUtility.CreateLoanGivenAccountWithSchedules("ACC-002-9827387",startDate, endDate,codeGenService);

        ObjectMapper mapper = ObjectMapperConfiguration.getObjectMapper();

        account.processTransaction("Advance", BigDecimal.valueOf(100));

        Position currentPosition = account.getPositions().get("Principal");

        assertThat(currentPosition.getAmount(), is(BigDecimal.valueOf(100.00).setScale(2,BigDecimal.ROUND_HALF_DOWN)));

    }

    @Test
    public void TestSchedules()
    {
        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears(25);

        Account account = TestUtility.CreateLoanGivenAccountWithSchedules("ACC-002-9827287",startDate, endDate,codeGenService);

        Schedule accrualSchedule = account.getSchedules().get("AccrualSchedule");

        Schedule interestSchedule = account.getSchedules().get("InterestSchedule");

        LocalDate interestStart =  LocalDate.of(2013, 3, 31);

        interestSchedule.setStartDate(interestStart);
        interestSchedule.setEndDate(endDate);

        interestSchedule.getIncludeDates().add(new ScheduleDate(endDate));

        List<LocalDate> accrualDates = accrualSchedule.GetAllDates(endDate);

        assertThat(accrualDates.get(0), is(startDate));
        assertThat(accrualDates.get(accrualDates.size()-1), is(endDate));
        assertThat((long)accrualDates.size(), is(ChronoUnit.DAYS.between(startDate, endDate)+1));

        List<LocalDate> interestDates = interestSchedule.GetAllDates(endDate);

        assertThat(interestDates.get(0), is(interestStart));
        assertThat(interestDates.get(interestDates.size()-1), is(endDate));

        assertThat(interestDates.get(1), is(LocalDate.of (2013,4,30)));
        assertThat(interestDates.get(2), is(LocalDate.of (2013,5,31)));
        assertThat(interestDates.get(11), is(LocalDate.of (2014,2,28)));

    }

    @Test
    public void TestForecast(){

        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears(25);

        Account account = TestUtility.CreateLoanGivenAccountWithSchedules("ACC-002-9327387",startDate, endDate,codeGenService);

        account.valueDate = startDate;

        account.forecast(endDate);

        assertThat(account.getPositions().get("Principal").getAmount(), is(BigDecimal.valueOf(1333778.93)));
        assertThat(account.getPositions().get("InterestCapitalized").getAmount(), is(BigDecimal.valueOf(709778.93)));
        assertThat( account.getPositions().get("InterestAccrued").getAmount().compareTo(BigDecimal.valueOf(0.005)), is(-1));

    }

    @Test
    public void TestPaymentCalc(){
        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears(25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();

        Account prototype = TestUtility.CreateLoanGivenAccountWithSchedules("ACC-002-0982397", startDate, endDate,codeGenService);

        Account account = new localLoanGiven(prototype);

        account.valueDate = startDate;

        account.businessDayCalculator = calendar;

        account.setCalculated();

        account.calculateInstaments();

        InstalmentSet instalments = account.getInstalmentSets().get("Redemptions");

        List<LocalDate> dateList = new ArrayList<>(instalments.getInstalments().keySet());

        Collections.sort(dateList);

        assertThat(dateList.size(), greaterThan(0));

        BigDecimal firstAmount = instalments.getInstalments().get(dateList.get(0)).getAmount();
        BigDecimal lastAmount = instalments.getInstalments().get(dateList.get(dateList.size()-1)).getAmount();

        BigDecimal difference1 = firstAmount.subtract(BigDecimal.valueOf( 2964.37)).abs();
        BigDecimal difference2 = lastAmount.subtract(BigDecimal.valueOf( 2964.37)).abs();

        assertThat(difference1.compareTo(BigDecimal.valueOf(0.01)), is(-1));
        assertThat(difference2.compareTo(BigDecimal.valueOf(0.01)), is(-1));

    }

    /*public void TestPaymentCalc()
            {
                DateTime startDate = new DateTime(2013, 3, 8);
                DateTime endDate = startDate.AddYears(25);
                Calendar calendar = Utility.CreateEuroZoneCalendar();

                SessionState.Current.ValueDate = startDate;

                var account = CreateLoanGivenAccount(startDate, endDate, calendar);

                var client = AccountFactory.CreateTransactionClient(Utility.CreateLoanGivenAccountType(), account);

                //var client = new TransactRules.Runtime.LoanGiven { Account = account };

                client.Initialize();

                var accrualSchedule = account.GetSchedule("AccrualSchedule");
                var interestSchedule = account.GetSchedule("InterestSchedule");
                var redemptionSchedule = account.GetSchedule("RedemptionSchedule");

                DateTime interestStart = new DateTime(2013, 3, 31);

                interestSchedule.StartDate = interestStart;
                interestSchedule.EndDate = endDate;
                interestSchedule.IncludeDates.Add(new ScheduleDate { Value = endDate });

                redemptionSchedule.StartDate = interestStart;
                redemptionSchedule.EndDate = endDate;
                redemptionSchedule.IncludeDates.Add(new ScheduleDate { Value = endDate });

                var accrualOption = account.GetOption("AccrualOption");

                accrualOption.Value = "365";

                client.CalculateInstaments();

                var instalments = account.GetInstalments("Redemptions");

                Assert.IsTrue(Math.Abs((decimal)2964.37  - instalments.First().Amount) <(decimal) 0.01);
                Assert.IsTrue(Math.Abs((decimal)2964.37 - instalments.Last().Amount) < (decimal)0.01);
            }*/



}
