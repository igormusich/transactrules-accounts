package com.transactrules.accounts.services;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
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

    @Override
    public Account create(String accountTypeName, String accountNumber) {

        /*Iterable<AccountType> iterable = accountTypeRepository.findAll();

        Iterator<AccountType> iterator = iterable.iterator();

        List<AccountType> accountTypes = new ArrayList<>();
        iterator.forEachRemaining(accountTypes::add);


        AccountType accountType = accountTypes.stream().filter(at-> at.getName().equalsIgnoreCase(accountTypeName)).findFirst().get();
*/
        AccountType accountType = accountTypeRepository.findByName(accountTypeName).get(0);

        Account account = new Account(accountType, accountNumber);

        account.initialize(accountType);

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
