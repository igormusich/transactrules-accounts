package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.runtime.Transaction;
import com.transactrules.accounts.services.AccountService;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private AccountType accountType;
    private Account account;

    private final String accountTypeName = "AccountType_AccountServiceTest";
    private final String accountNumber = "AC-account-service-test";

    @Before
    public void setup() {

        accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName(accountTypeName);

        accountTypeRepository.save(accountType);

        account = TestUtility.CreateLoanGivenAccount(accountNumber, LocalDate.now(),LocalDate.now().plusYears(10),codeGenService);

        account.setAccountTypeName(accountTypeName);

        accountService.create(account);

    }

    @Test
    public void createTransactionTest() throws InterruptedException {

        BigDecimal principal =  account.getPositions().get("Principal").getAmount();

        accountService.createTransaction(new Transaction(accountNumber,"AdditionalAdvance", BigDecimal.valueOf(100), LocalDate.now(),LocalDate.now()));

        Account updatedAccount = accountService.findByAccountNumber(accountNumber);

        assertThat(updatedAccount.getPositions().get("Principal").getAmount(), is(principal.add(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_DOWN))));

    }
}
