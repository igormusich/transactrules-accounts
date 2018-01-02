package com.transactrules.accounts.runtime;

import com.transactrules.accounts.StartupApplicationRunner;
import com.transactrules.accounts.configuration.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountFactory {

    @Autowired
    CodeGenService codeGenService;

    private Logger logger = LoggerFactory.getLogger(StartupApplicationRunner.class);

    public Account createAccount(AccountType accountType)  {

        Account account = null;

        try{
            Class accountClass = codeGenService.getAccountClass(accountType);

            account = (Account) accountClass.newInstance();

            account.initialize(accountType);
        } catch (Exception ex){
            logger.error(ex.toString());
        }

        return account;
    }

}
