package com.transactrules.accounts.runtime.accounts;


import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.DateType;
import com.transactrules.accounts.configuration.TransactionType;
import com.transactrules.accounts.runtime.Position;
import com.transactrules.accounts.runtime.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AccountValuation
{
    protected Account account;
    protected AccountType accountType;
    protected LocalDate actionDate;
    protected LocalDate valueDate;


    public List<Transaction> transactions() {

        return account.getTransactions();
    }

    public Map<String,Position> positions() {

        return account.getPositions();
    }


    public void initialize(Account account, AccountType accountType) {
        this.account = account;
        this.accountType = accountType;
        actionDate = LocalDate.now();
        valueDate = LocalDate.now();
        account.initialize(accountType);
    };

    public abstract void processTransaction(Transaction transaction);

    public abstract void startOfDay();
    public abstract void endOfOfDay();

    public abstract void onDataChanged();

    public abstract void calculateInstaments();

    public Transaction createTransaction(TransactionType transactionType, BigDecimal amount) {

        Transaction transaction = new Transaction(transactionType,amount,account, actionDate, valueDate);

        processTransaction(transaction);

        account.addTransaction(transaction);

        return transaction;
    }

    public void forecast(LocalDate futureDate)
    {
        LocalDate originalValueDate = valueDate;

        LocalDate iterator = originalValueDate;

        if (!account.isActive())
        {
            startOfDay();
        }

        while (valueDate.isBefore(futureDate) || valueDate.isEqual(futureDate) )
        {
            endOfOfDay();

            valueDate = valueDate.plusDays(1);

            startOfDay();
        }

        valueDate = originalValueDate;
    }

    public LocalDate StartDate(){

        DateType dateType = accountType.getDateTypeByName("StartDate").get();

        return account.getDates().get(dateType.getId()).getDate();

    }

    public LocalDate ValueDate(){

        return valueDate;
    }

    /*public void SetFutureInstalmentValue(string instalmentType, ScheduledTransactionTiming timing, decimal getValue)
    {
        foreach (var instalment in Account.GetInstalments(instalmentType))
        {
            if (!instalment.HasFixedValue)
            {
                if ( (timing == ScheduledTransactionTiming.StartOfDay && instalment.ValueDate > SessionState.Current.ValueDate)
                        || (timing == ScheduledTransactionTiming.EndOfDay && instalment.ValueDate >= SessionState.Current.ValueDate))
                {
                    instalment.Amount = getValue;
                }
            }
        }
    }*/
}
