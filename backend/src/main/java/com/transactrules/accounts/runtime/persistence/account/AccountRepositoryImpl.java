package com.transactrules.accounts.runtime.persistence.account;

import com.transactrules.accounts.runtime.domain.Account;
import com.transactrules.accounts.runtime.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<AccountDataObject> dataObject=  repository.findById(key);

        if(!dataObject.isPresent()){
            return null;
        }

        return dataObject.get().getAccount();
    }

}
