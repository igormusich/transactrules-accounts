package com.transactrules.accounts.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeRepository;
import com.transactrules.accounts.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    CalendarRepository calendarRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account create(Account prototype)  {

        AccountType accountType = accountTypeRepository.findOne(prototype.getAccountTypeName());

        AccountBuilder builder = new AccountBuilder(accountType, prototype.getAccountNumber(), codeGenService );

        for (String name: prototype.getDates().keySet()) {
            builder.addDateValue(name, prototype.getDates().get(name));
        }

        for (String name: prototype.getAmounts().keySet()) {
            builder.addAmountValue(name, prototype.getAmounts().get(name));
        }

        for (String name: prototype.getRates().keySet()) {
            builder.addRateValue(name, prototype.getRates().get(name));
        }

        for (String name: prototype.getOptions().keySet()) {
            builder.addOptionValue(name, prototype.getOptions().get(name));
        }

        for (String name: prototype.getSchedules().keySet()) {
            builder.addSchedule(name, prototype.getSchedules().get(name));
        }

        Account account = builder.getAccount();

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

    @Override
    public Transaction createTransaction(Transaction transaction) throws InterruptedException {

        //TransactionManager txManager = new TransactionManager(amazonDynamoDB, "Transaction", "Account");

        //com.amazonaws.services.dynamodbv2.transactions.Transaction tx = txManager.newTransaction();

        try {

            Account dbAccount = accountRepository.findOne(transaction.getAccountNumber());
            AccountType accountType = accountTypeRepository.findOne(dbAccount.getAccountTypeName());

            //Class accountClass = codeGenService.getAccountClass(accountType);

            //Account account = (Account) accountClass.newInstance();

            Account account = new localLoanGiven();

            account.initializeFromPrototype(dbAccount);

            account.setCalculated();

            //this should update the balances
            account.createTransaction(transaction);

            accountRepository.save(account);

            transactionRepository.save(transaction);

            //tx.commit();
        }
        catch (Exception ex){
            //tx.delete();
            logger.error(ex.toString());
        }


        return transaction;
    }

}
