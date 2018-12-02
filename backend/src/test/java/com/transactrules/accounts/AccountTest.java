package com.transactrules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.metadata.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.domain.*;
import com.transactrules.accounts.runtime.repository.CalendarRepository;
import com.transactrules.accounts.utilities.Utility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Administrator on 11/26/2016.
 */

public class AccountTest extends BaseIntegrationTest {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    CodeGenService codeGenService;

    private AccountType savingsAccountType = TestConfiguration.createSavingsAccountType();
    private AccountType loanGivenAccountType= TestConfiguration.createLoanGivenAccountType();
    private Calendar calendar = TestConfiguration.CreateEuroZoneCalendar();


    @Before
    public void initialize()
    {

        accountTypeRepository.save(savingsAccountType);
        accountTypeRepository.save(loanGivenAccountType);
        calendarRepository.save(calendar);
    }

    @Test
    public void ProcessTransaction_deposit(){

        Class accountClass = codeGenService.getAccountClass(savingsAccountType);

        AccountBuilder builder = new AccountBuilder(savingsAccountType.getClassName(), "ACC-002-98392", accountClass);

        Account account = builder.getAccount();

        account.createTransaction("Deposit", BigDecimal.valueOf(100));

        Position currentPosition = account.getPositions().get("Current");

        assertThat(currentPosition.getAmount(), is(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_DOWN)));
    }

    @Test
    public void copyConstructor(){

        Account prototype = createPrototype (loanGivenAccountType,calendar, LocalDate.of (2013, 3, 31), LocalDate.of (2023, 3, 31),"ACC-Test-Copy-Constructor");

        prototype.actionDate = LocalDate.now();

        prototype.activate();

        Account copyAccount = new Account(prototype);

        String prototypeString = Utility.getYaml(prototype);

        String copyString = Utility.getYaml(copyAccount);

        assertThat(copyString, is(prototypeString));

    }

    private Account createPrototype(AccountType accountType, Calendar calendar, LocalDate startDate, LocalDate endDate,String accountNumber) {
        Class accountClass = codeGenService.getAccountClass(accountType);

        AccountBuilder builder = new AccountBuilder( accountType.getClassName(), accountNumber, accountClass );

        builder.setBusinessDayCalculator(calendar)
                .addDateValue("StartDate", startDate)
                .addDateValue("AccrualStart",startDate)
                .addDateValue("EndDate", endDate)
                .addAmountValue("AdvanceAmount", BigDecimal.valueOf(624000), startDate)
                .addRateValue("InterestRate", BigDecimal.valueOf(3.04/100), startDate)
                .addOptionValue("AccrualOption", "365")
        ;

        Account prototype =  builder.getAccount();

        prototype.setCalculated();

        Schedule interestSchedule = prototype.getSchedules().get("InterestSchedule");
        Schedule redemptionSchedule = prototype.getSchedules().get("RedemptionSchedule");

        interestSchedule.setStartDate(startDate);
        interestSchedule.setEndDate(endDate);
        interestSchedule.getIncludeDates().add(new ScheduleDate(endDate) );

        redemptionSchedule.setStartDate(startDate);
        redemptionSchedule.setEndDate(endDate);
        redemptionSchedule.getIncludeDates().add(new ScheduleDate (endDate) );

        return prototype;
    }
}
