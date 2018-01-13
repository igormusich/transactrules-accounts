package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeServiceImpl(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository =accountTypeRepository;
    }

    @Override
    public AccountType create(AccountType accountType)  {

        AccountType result = accountTypeRepository.save(accountType);

        return result;
    }

    @Override
    public AccountType save(AccountType accountType) {
        AccountType result = accountTypeRepository.save(accountType);

        return result;
    }

    @Override
    public List<AccountType> findAll() {
        Iterable<AccountType> items = accountTypeRepository.findAll();

        Iterator<AccountType> iterator = items.iterator();

        List<AccountType> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);

        return list;
    }

    @Override
    public AccountType findByClassName(String className) {
        AccountType result = accountTypeRepository.findOne(className);

        return result;
    }
}
