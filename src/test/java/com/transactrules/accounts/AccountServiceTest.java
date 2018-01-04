package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

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

    private final String accountTypeName = "AccountType_AccountServiceTest";

    @Before
    public void setup() {

        accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setName(accountTypeName);

        accountTypeRepository.save(accountType);

    }

    @Test
    public void createLoanAccountTest(){
        Account account = TestUtility.CreateLoanGivenAccount("AC-002-043243", LocalDate.now(),LocalDate.now().plusYears(10),codeGenService);

        account.setAccountTypeName(accountTypeName);

        accountService.create(account);
    }
}
