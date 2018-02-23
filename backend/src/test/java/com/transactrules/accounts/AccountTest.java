package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.metadata.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.domain.Account;
import com.transactrules.accounts.runtime.domain.AccountBuilder;
import com.transactrules.accounts.runtime.domain.CodeGenService;
import com.transactrules.accounts.runtime.domain.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Administrator on 11/26/2016.
 */

public class AccountTest extends BaseIntegrationTest {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    CodeGenService codeGenService;

    private AccountType savingsAccountType = AccountTypeFactory.createSavingsAccountType();

    @Before
    public void initialize()
    {

        accountTypeRepository.save(savingsAccountType);
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
}
