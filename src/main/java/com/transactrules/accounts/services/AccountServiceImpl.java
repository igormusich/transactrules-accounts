package com.transactrules.accounts.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountRepository;
import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    UniqueIdService uniqueIdService;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account save(Account prototype)  {

        AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());

        AccountBuilder builder = new AccountBuilder(prototype, accountTypeService, codeGenService );

        Account account = builder.getAccount();

        account= accountRepository.save(account);

        return account;
    }

    @Override
    public Account create(AccountType accountType) {

        String accountNumber = uniqueIdService.getNextId("Account");

        AccountBuilder builder = new AccountBuilder(accountType, accountNumber, this.codeGenService);

        Account account = builder.getNewAccount();

        return account;
    }

    @Override
    public Account calculateProperties(Account account) {
        AccountBuilder accountBuilder = new AccountBuilder(account, accountTypeService, codeGenService);

        Account calculatedAccount = accountBuilder.getAccount();

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
            AccountType accountType = accountTypeService.findByClassName(dbAccount.getAccountTypeName());

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Class accountClass = null;
            Account account = null;
            try {
                PrintWriter writer = new PrintWriter(os);
                accountClass =  codeGenService.getAccountClass(accountType, writer);
                account = (Account) accountClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                String aString = "";

                try {
                    aString = new String(os.toByteArray(),"UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    logger.error("Error converting output byte array to string",e1);
                }
                logger.error(aString, e);
            }

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
