package com.transactrules.accounts;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import com.transactrules.accounts.configuration.PositionType;
import com.transactrules.accounts.configuration.TransactionType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AccountFactory;
import com.transactrules.accounts.runtime.AccountValuationService;
import com.transactrules.accounts.runtime.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Administrator on 11/26/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountFactory accountFactory;


    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private AccountValuationService accountValuationService;

    private AccountType savingsAccountType = AccountTypeFactory.createSavingsAccountType();

    @Before
    public void initialize()
    {

        accountTypeRepository.save(savingsAccountType);
    }

    @Test
    public void ProcessTransaction_deposit(){

        Account account = accountFactory.createAccount(savingsAccountType);

        Optional<TransactionType> depositTransactionType = savingsAccountType.getTransactionTypeByName("Deposit");
        Optional<PositionType> currentPositionType = savingsAccountType.getPositionTypeByName("Current");

        assertThat(depositTransactionType.isPresent(), is(true));
        assertThat(currentPositionType.isPresent(), is(true));

        //accountValuationService.initialize(account);

        account.createTransaction(depositTransactionType.get(), BigDecimal.valueOf(100));

        Position currentPosition = account.getPositions().get(currentPositionType.get().getName());

        assertThat(currentPosition.getAmount(), is(BigDecimal.valueOf(100)));
    }
}
