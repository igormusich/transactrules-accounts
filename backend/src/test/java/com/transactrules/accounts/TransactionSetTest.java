package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.runtime.domain.*;
import com.transactrules.accounts.runtime.service.AccountService;
import com.transactrules.accounts.metadata.service.AccountTypeService;
import com.transactrules.accounts.runtime.service.SystemPropertyService;
import com.transactrules.accounts.runtime.service.TransactionService;
import com.transactrules.accounts.utilities.Utility;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TransactionSetTest extends BaseIntegrationTest {

    private final  String ACCOUNT_NUMBER = "ACC-002-2093800927";
    private Account account;
    private AccountType accountType;
    private LocalDate endDate;
    private LocalDate startDate;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    SystemPropertyService properties;

    @Before
    public void setUp() throws Exception {

        startDate = LocalDate.of(2013, 3, 8);
        endDate = startDate.plusYears(25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();

        account = TestUtility.CreateLoanGivenAccountWithSchedules(ACCOUNT_NUMBER, startDate, endDate,codeGenService);

        account.valueDate = startDate;

        account.businessDayCalculator = calendar;

        account.setCalculated();

        account.calculateInstaments();

        account.setTransactions(new ArrayList<>());

        accountType = TestConfiguration.createLoanGivenAccountType();

    }

    @Test
    public void setIdFormat() {
        TransactionSet set = new TransactionSet(ACCOUNT_NUMBER, LocalDate.of(2018,2,1),1);

        assertThat(set.getId(), is(ACCOUNT_NUMBER + "-2018-02-001"));
    }

    @Test
    public void createBasedOnMixedSet() {
        //TransactionSet set = new TransactionSet(ACCOUNT_NUMBER, LocalDate.of(2018,2,1),1);

        List<Transaction> items = getTransactionMix();

        List<TransactionSet> sets = transactionService.save(accountType, UUID.randomUUID().toString(),items);

        assertThat(sets.size(), is(1));

        TransactionSet set = sets.get(0);

        //set.getData().appendTransactions(items, TestUtility.CreateLoanGivenAccountType());

        assertThat(set.getData().getCount(), is(8));
        assertThat(set.getData().getRepeatableLists().size(), is(3));


        String yaml = Utility.getYaml(set);
    }


    @Test
    public void forecast() {
        //TransactionSet set = new TransactionSet(ACCOUNT_NUMBER, LocalDate.of(2018,2,1),1);

        account.setTransactions(new ArrayList<>());
        account.actionDate = properties.getActionDate();
        account.forecast(endDate);

        List<TransactionSet> sets = transactionService.save(accountType, UUID.randomUUID().toString(),account.getTransactions());

        assertThat(sets.size(), is(5));

        TransactionSet set = sets.get(0);

        //set.getData().appendTransactions(items, TestUtility.CreateLoanGivenAccountType());

        assertThat(set.getData().getCount(), is(2000));
        //assertThat(set.getData().getRepeatableLists().size(), is(1));

    }


    private List<Transaction> getTransactionMix() {

        LocalDate actionDate =  properties.getActionDate();

        List<Transaction> items = new ArrayList<>();


        //28 accrual transactions in 3 sets
        items.addAll(
                generateRange(
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),1),
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),10),
                        BigDecimal.valueOf(3.45876),
                        "InterestAccrued"));

        items.addAll(
                generateRange(
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),11),
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),20),
                        BigDecimal.valueOf(3.89744),
                        "InterestAccrued"));

        items.addAll(
                generateRange(
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),21),
                        LocalDate.of(actionDate.getYear(),actionDate.getMonth(),28),
                        BigDecimal.valueOf(3.49809),
                        "InterestAccrued"));

        //5 transactions per month
        items.add(new Transaction(
                "Advance",
                BigDecimal.valueOf(1000.00),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),1),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),1)));

        items.add(new Transaction(
                "Redemption",
                BigDecimal.valueOf(100),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),11),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),11)));

        items.add(new Transaction(
                "Redemption",
                BigDecimal.valueOf(150),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),21),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),21)));

        items.add(new Transaction(
                "EarlyRedemptionFee",
                BigDecimal.valueOf(1.50),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),28),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),28)));

        items.add(new Transaction(
                "InterestCapitalized",
                BigDecimal.valueOf(2.98),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),28),
                LocalDate.of(actionDate.getYear(),actionDate.getMonth(),28)));

        //3 transactions out of range for the set

        LocalDate previousMonth = LocalDate.of(actionDate.getYear(),actionDate.getMonth(),1).minusDays(1);

        items.add(new Transaction(
                "EarlyRedemptionFee",
                BigDecimal.valueOf(1.50),
                previousMonth,
                previousMonth));

        LocalDate nextMonthStart = previousMonth.plusMonths(2);
        LocalDate nextMonthEnd = nextMonthStart.plusMonths(1).minusDays(1);

        items.add(new Transaction(
                "Redemption",
                BigDecimal.valueOf(150),
                nextMonthStart,
               nextMonthStart));

        items.add(new Transaction(
                "InterestCapitalized",
                BigDecimal.valueOf(3.21),
                nextMonthEnd,
                nextMonthEnd));

        Collections.shuffle(items);
        return items;
    }

    private List<Transaction> generateRange( LocalDate fromDate, LocalDate toDate, BigDecimal amount, String transactionType){
        LocalDate iter = fromDate;

        List<Transaction> items = new ArrayList<>();

        while(iter.isBefore(toDate) || iter.isEqual(toDate)){
            items.add(new Transaction(transactionType,amount, iter, iter));
            iter = iter.plusDays(1);
        }

        return items;
    }
}