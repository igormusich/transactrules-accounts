package com.transactrules.accounts.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.TransactionRuleType;
import com.transactrules.accounts.metadata.TransactionType;
import com.transactrules.accounts.repository.AccountRepository;
import com.transactrules.accounts.runtime.*;
import com.transactrules.accounts.runtime.Calendar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    UniqueIdService uniqueIdService;

    @Autowired
    SystemPropertyService properties;

    @Autowired
    TransactionService transactionService;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account save(Account prototype) {

        Account account;

        if (prototype.isActive()) {
            account = this.activate(prototype);
        } else {
            account = getAccountFromPrototype(prototype, null);
            account = accountRepository.save(account);
        }

        return account;
    }

    @Override
    public Account create(AccountType accountType) {

        String accountNumber = uniqueIdService.getNextId("Account");

        Class accountClass = codeGenService.generateClass(accountType);

        AccountBuilder builder = new AccountBuilder(accountType.getClassName(), accountNumber, accountClass);

        Account account = builder.getNewAccount();

        return account;
    }

    @Override
    public Account calculateProperties(Account prototype) {
        AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());

        Class accountClass = codeGenService.generateClass(accountType);

        AccountBuilder builder = new AccountBuilder(prototype, accountClass);

        Account account = builder.getAccount();

        return account;
    }

    @Override
    public Account calculateInstalments(Account prototype) {

        AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());

        Class accountClass = codeGenService.generateClass(accountType);

        AccountBuilder builder = new AccountBuilder(prototype, accountClass);

        builder.setBusinessDayCalculator(getCalendarForAccount(prototype));

        Account account = builder.getAccount();

        account.valueDate = account.retrieveStartDate();

        account.calculateInstaments();

        return account;
    }

    private Calendar getCalendarForAccount(Account account) {

        Calendar calendar;

        if (account.getCalendarNames().size() > 0) {
            calendar = calendarService.findByName(account.getCalendarNames().get(0));
        } else {
            calendar = calendarService.getDefault();
        }

        return calendar;
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
    public Transaction createTransaction(String accountNumber, Transaction transaction) throws InterruptedException {

        //TransactionManager txManager = new TransactionManager(amazonDynamoDB, "Transaction", "Account");

        //com.amazonaws.services.dynamodbv2.transactions.Transaction tx = txManager.newTransaction();

        try {

            Account prototype = accountRepository.findOne(accountNumber);
            Account account = getAccountFromPrototype(prototype, getCalendarForAccount(prototype));
            AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());

            //this should update the balances
            account.createTransaction(transaction);

            accountRepository.save(account);

            transactionService.save(accountType, accountNumber, transaction);

            //tx.commit();
        } catch (Exception ex) {
            //tx.delete();
            logger.error(ex.toString());
        }


        return transaction;
    }

    @NotNull
    private Account getAccountFromPrototype(Account prototype, Calendar calendar) {

        AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());
        Class accountClass = codeGenService.generateClass(accountType);

        AccountBuilder builder = new AccountBuilder(prototype, accountClass);

        builder.setBusinessDayCalculator(calendar);

        Account account = builder.getAccount();

        return account;
    }


    @Override
    public Account activate(Account prototype) {


        Account account = getAccountFromPrototype(prototype, getCalendarForAccount(prototype));
        AccountType accountType = accountTypeService.findByClassName(prototype.getAccountTypeName());

        //for all dates between startDate and actionDate run start and end of day

        if (account.retrieveStartDate().isBefore(properties.getActionDate())) {
            account.actionDate = properties.getActionDate();
            account.valueDate = account.retrieveStartDate();
            account.forecast(properties.getActionDate());
        }

        account.activate();

        accountRepository.save(account);

        transactionService.save(accountType, account.getAccountNumber(), account.getTransactions());

        return account;
    }

    @Override
    public List<Transaction> findTransactions(String accountNumber, LocalDate fromDate, LocalDate toDate) {
        Account account = findByAccountNumber(accountNumber);

        if(!account.isActive() || toDate.isBefore(fromDate)){
            //inactive accounts have no transactions
            return new ArrayList<>();
        }

        //start and end date define date range for which we have transaction sets
        LocalDate startDate = account.getDateActivated();
        LocalDate endDate = properties.getActionDate();

        //narrow down transactionSet range

        if(fromDate.isAfter(startDate)){
            startDate = fromDate;
        }

        if(toDate.isBefore(endDate)){
            endDate = toDate;
        }

        List<Transaction> transactions = getTransactions(accountNumber, fromDate, toDate, startDate, endDate, t-> true );

        return transactions;
    }

    @NotNull
    private List<Transaction> getTransactions(String accountNumber, LocalDate fromDate, LocalDate toDate, LocalDate startDate, LocalDate endDate, Predicate<Transaction> condition) {
        LocalDate iter = LocalDate.of(startDate.getYear(), startDate.getMonthValue(),1);

        List<Transaction> transactions = new ArrayList<>();

        while(iter.isBefore(endDate)){
            String transactionSetId = TransactionSet.generateId(accountNumber, iter, 1);

            TransactionSet set = transactionService.getTransactionSet(transactionSetId);

            transactions.addAll( set.getTransactions().stream().filter( condition.and(t-> isBetween(t.getActionDate(), fromDate, toDate)) ).collect(Collectors.toList()));

            while(set.getNextId()!=null){
                set = transactionService.getTransactionSet(set.getNextId());
                transactions.addAll( set.getTransactions().stream().filter( condition.and(t-> isBetween(t.getActionDate(), fromDate, toDate)) ).collect(Collectors.toList()));
            }

            iter = iter.plusMonths(1);
        }

        transactions.sort(Comparator.comparing(Transaction::getActionDate));
        return transactions;
    }

    private List<String> getTransactionTypesAffectingPositionType(AccountType accountType, String positionType){
        List<String> transactionTypes = new ArrayList<>();

        for(TransactionType transactionType: accountType.getTransactionTypes()){
            for(TransactionRuleType ruleType: transactionType.getTransactionRules()){
                if(ruleType.getPositionTypeName().equalsIgnoreCase(positionType)){
                    transactionTypes.add(transactionType.getPropertyName());
                }
            }
        }

        return transactionTypes;
    }

    @Override
    public List<Transaction> getTransactionTrace(String accountNumber, LocalDate fromDate, LocalDate toDate, String positionType1, String positionType2) {
        Account account = findByAccountNumber(accountNumber);

        if(!account.isActive() || toDate.isBefore(fromDate)){
            //inactive accounts have no transactions
            return new ArrayList<>();
        }

        AccountType accountType = accountTypeService.findByClassName(account.getAccountTypeName());

        Predicate<Transaction> p = t-> true;

        if(positionType1 !=null){
            List<String> transactionTypes = getTransactionTypesAffectingPositionType(accountType,positionType1);

            Predicate<Transaction> position1Predicate = t -> transactionTypes.contains(t.getTransactionTypeName());

            p = p.and(position1Predicate);
        }

        if(positionType2 !=null){
            List<String> transactionTypes = getTransactionTypesAffectingPositionType(accountType,positionType1);

            Predicate<Transaction> position2Predicate = t -> transactionTypes.contains(t.getTransactionTypeName());

            p = p.and(position2Predicate);
        }

        //start and end date define date range for which we have transaction sets
        LocalDate startDate = account.getDateActivated();
        LocalDate endDate = properties.getActionDate();

        //narrow down transactionSet range

        if(fromDate.isAfter(startDate)){
            startDate = fromDate;
        }

        if(toDate.isBefore(endDate)){
            endDate = toDate;
        }

        List<Transaction> transactions =  getTransactions(accountNumber, fromDate, toDate, startDate, endDate, p );

        return transactions;
    }

    private Boolean isBetween(LocalDate item, LocalDate from, LocalDate to){
        return ( item.isAfter(from) || item.isEqual(from)) && (item.isBefore(to) || item.isEqual(to));
    }

    @Override
    public void startOfDay() {

        Map<String, AccountType> accountTypeMap = getAccountTypeMap();
        Map<String, Class> classMap = getClassMap();

        for (Account prototype : accountRepository.findAll()) {
            Class accountClass = classMap.get(prototype.getAccountTypeName());

            AccountBuilder builder = new AccountBuilder(prototype, accountClass);

            Account account = builder.getAccount();

            account.valueDate = properties.getActionDate();
            account.actionDate = properties.getActionDate();

            account.startOfDay();

            accountRepository.save(account);

            AccountType accountType = accountTypeMap.get(account.getAccountTypeName());

            transactionService.save(accountType, account.getAccountNumber(), account.getTransactions());
        }

    }

    @Override
    public void endOfDay() {

        Map<String, AccountType> accountTypeMap = getAccountTypeMap();
        Map<String, Class> classMap = getClassMap();

        for (Account prototype : accountRepository.findAll()) {
            Class accountClass = classMap.get(prototype.getAccountTypeName());

            AccountBuilder builder = new AccountBuilder(prototype, accountClass);

            Account account = builder.getAccount();

            account.valueDate = properties.getActionDate();
            account.actionDate = properties.getActionDate();

            account.endOfOfDay();

            accountRepository.save(account);

            AccountType accountType = accountTypeMap.get(account.getAccountTypeName());
            transactionService.save(accountType, account.getAccountNumber(), account.getTransactions());
        }

        properties.incrementActionDate();
    }

    private Map<String, AccountType> getAccountTypeMap() {
        Map<String, AccountType> map = new HashMap<>();

        for (AccountType accountType : accountTypeService.findAll()) {
            map.put(accountType.getClassName(), accountType);
        }

        return map;
    }

    private Map<String, Class> getClassMap() {
        Map<String, Class> map = new HashMap<>();

        for (AccountType accountType : accountTypeService.findAll()) {
            map.put(accountType.getClassName(), codeGenService.generateClass(accountType));
        }

        return map;
    }

}
