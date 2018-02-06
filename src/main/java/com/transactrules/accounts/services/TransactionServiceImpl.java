package com.transactrules.accounts.services;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.TransactionSetRepository;
import com.transactrules.accounts.runtime.RepeatableTransactionList;
import com.transactrules.accounts.runtime.Transaction;
import com.transactrules.accounts.runtime.TransactionSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionSetRepository transactionSetRepository;

    @Autowired
    SystemPropertyService properties;


    @Override
    public List<TransactionSet> save(AccountType accountType, String accountNumber, Transaction transaction) {


        List<Transaction> transactions = new ArrayList<>();

        transactions.add(transaction);

       return save(accountType, accountNumber, transactions);

    }

    @Override
    public List<TransactionSet> save(AccountType accountType, String accountNumber, List<Transaction> transactions) {

        LocalDate actionDate = properties.getActionDate();

        String id = TransactionSet.generateId(accountNumber,actionDate,1);

        TransactionSet transactionSet = transactionSetRepository.findOne(id);

        if(transactionSet == null) {
            transactionSet = new TransactionSet(accountNumber,actionDate,1);
        }

        List<TransactionSet> changedSets =appendTransactions(transactionSet, transactions, accountType);

        transactionSetRepository.save(changedSets);

        return changedSets;
    }

    public List<TransactionSet> appendTransactions( TransactionSet transactionSet, List<Transaction> transactions, AccountType accountType)
        {
            //sort once, otherwise expensive operation in recursive function
            transactions.sort(Comparator.comparing(Transaction::getActionDate));

            Transaction[] transactionsArray = new Transaction[transactions.size()];

            transactions.toArray(transactionsArray);


            List<TransactionSet> changedSets = new ArrayList<>();

              innerAppendTransactions(changedSets, transactionSet, transactionsArray,0,accountType);

              return changedSets;
        }


    public void innerAppendTransactions(List<TransactionSet> changedSets, TransactionSet transactionSet, Transaction[] transactions , int index, AccountType accountType) {
        //assume that accrual transactions with same action date and value date need to be compressed


        for(Integer i=index; i< transactions.length; i++){

            Transaction transaction = transactions[i];

            if (!transactionSet.belongsToSet(transaction)) {
                continue;
            }

            if( transactionSet.getData().isAtCapacity()){

                LocalDate actionDate = getStartDate(transactionSet);

                if(transactionSet.getNextId()!=null){
                    //pass adding of new transactions to next set in the chain
                    TransactionSet nextTransacionSet = transactionSetRepository.findOne(transactionSet.getNextId());
                    innerAppendTransactions(changedSets,nextTransacionSet, transactions, i, accountType);
                    return;
                }

                //create next set in the chain, this set is being modified to include link to next set
                changedSets.add(transactionSet);

                TransactionSet newTransactionSet = createNextSet(transactionSet, actionDate);

                innerAppendTransactions(changedSets, newTransactionSet, transactions, i, accountType);
                return ;
            }

            if (transactionSet.isRepeatable(accountType, transaction)) {
                Optional<RepeatableTransactionList> repeatableList = transactionSet.getExistingRepeatableList(transaction);

                if (transactionSet.listExistsAndAmountAndDateMatch(transaction, repeatableList)) {
                    repeatableList.get().add(transaction);
                } else {
                    RepeatableTransactionList newList = new RepeatableTransactionList(transaction);
                    transactionSet.getData().getRepeatableLists().add(newList);
                    transactionSet.getData().incrementCount();
                }
            } else {
                transactionSet.getData().getList().getItems().add(transaction);
                transactionSet.getData().incrementCount();
            }

        }

        changedSets.add(transactionSet);

        return;
    }

    @NotNull
    private LocalDate getStartDate(TransactionSet transactionSet) {
        return LocalDate.of( transactionSet.getData().getYear(), transactionSet.getData().getMonth(),1);
    }

    @NotNull
    private TransactionSet createNextSet(TransactionSet transactionSet, LocalDate actionDate) {
        TransactionSet newTransactionSet = new TransactionSet(transactionSet.getData().getAccountNumber(),actionDate, transactionSet.getData().getSetId()+1 );
        transactionSet.setNextId(newTransactionSet.getId());
        newTransactionSet.setPreviousId(transactionSet.getId());
        return newTransactionSet;
    }

}
