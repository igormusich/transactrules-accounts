package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Administrator on 11/26/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTypeRepositoryTest {

    @Autowired
    AccountTypeRepository repository;

    @Test
    public void CreateReadUpdateDelete_simple_account_type(){
        AccountType accountType = new AccountType("simple","simple");

        repository.save(accountType);


        AccountType retrieved = repository.findOne(accountType.getClassName());

        assertThat(retrieved.getClassName(),is("simple"));

        repository.delete(accountType.getClassName());

        boolean exists =  repository.exists(accountType.getClassName());

        assertThat(exists, is(false));
    }

    @Test
    public void CreateUpdateDelete_savings_account(){
        AccountType savingsAccount = AccountTypeFactory.createSavingsAccountType();

        repository.save(savingsAccount);

        AccountType  readAccountType = repository.findOne(savingsAccount.getClassName());

        assertThat( readAccountType.getPositionTypes().size(), is(2));

    }

}
