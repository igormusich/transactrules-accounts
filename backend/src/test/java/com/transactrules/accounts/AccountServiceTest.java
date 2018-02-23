package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.metadata.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.domain.Account;
import com.transactrules.accounts.runtime.domain.CodeGenService;
import com.transactrules.accounts.runtime.domain.Transaction;
import com.transactrules.accounts.runtime.service.AccountService;
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

public class AccountServiceTest extends BaseIntegrationTest {

    @Autowired
    AccountService accountService;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private AccountType accountType;
    private Account account;

    private final String accountTypeName = "AccountType_AccountServiceTest";
    private final String accountNumber = "AC-account-service-test-2";

    @Before
    public void setup() {

        accountType = TestConfiguration.createLoanGivenAccountType();
        accountType.setClassName(accountTypeName);

        accountTypeRepository.save(accountType);

        account = TestUtility.CreateLoanGivenAccount(accountNumber, LocalDate.now(),LocalDate.now().plusYears(10),codeGenService);

        account.setAccountTypeName(accountTypeName);

        accountService.save(account);

    }

    @Test
    public void createTransactionTest() throws InterruptedException {

        BigDecimal principal =  account.getPositions().get("Principal").getAmount();

        accountService.createTransaction(account.getAccountNumber(),  new Transaction("AdditionalAdvance", BigDecimal.valueOf(100), LocalDate.now(),LocalDate.now()));

        Account updatedAccount = accountService.findByAccountNumber(accountNumber);

        assertThat(updatedAccount.getPositions().get("Principal").getAmount(), is(principal.add(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_DOWN))));

    }
}
