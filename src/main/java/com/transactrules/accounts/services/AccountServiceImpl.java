package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AccountFactory;
import com.transactrules.accounts.runtime.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Autowired
    AccountFactory accountFactory;

    @Override
    public Account create(String accountTypeName, String accountNumber)  {

        AccountType accountType = accountTypeRepository.findByName(accountTypeName);

        Account account = accountFactory.createAccount(accountType); //new Account(accountType, accountNumber);

        account.setAccountNumber(accountNumber);

        account= accountRepository.save(account);

        return account;
    }

    @Override
    public List<Account> findAll() {
        Iterable<Account> items = accountRepository.findAll();

        Iterator<Account> iterator = items.iterator();

        List<Account> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);

        return list;
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findOne(accountNumber);

        return account;
    }
}
