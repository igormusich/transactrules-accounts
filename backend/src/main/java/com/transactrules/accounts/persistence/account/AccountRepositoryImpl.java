package com.transactrules.accounts.persistence.account;

import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountRepositoryImpl implements AccountRepository {
    @Autowired
    JpaAccountRepository repository;

    @Override
    public void save(Account account) {
        //since account might be generate class - convert to the base class

        Account saveAccount = new Account(account);
        saveAccount.setAccountNumber(account.getAccountNumber());
        saveAccount.setAccountTypeName(account.getAccountTypeName());

        repository.save(new AccountDataObject(saveAccount));
    }

    @Override
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();

        for(AccountDataObject  dataObject: repository.findAll()){
            list.add(dataObject.getAccount());
        }

        return list;
    }

    @Override
    public Account findOne(String key) {
        AccountDataObject dataObject=  repository.findOne(key);

        if(dataObject==null){
            return null;
        }

        return dataObject.getAccount();
    }

}
