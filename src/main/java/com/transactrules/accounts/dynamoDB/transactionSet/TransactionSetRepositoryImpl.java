package com.transactrules.accounts.dynamoDB.transactionSet;

import com.transactrules.accounts.repository.TransactionSetRepository;
import com.transactrules.accounts.runtime.TransactionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionSetRepositoryImpl implements TransactionSetRepository {

    @Autowired
    DynamoTransactionSetRepository dynamoRepository;

    @Override
    public void save(TransactionSet transactionSet) {
        dynamoRepository.save(new TransactionSetDataObject(transactionSet));
    }

    @Override
    public void save(Iterable<TransactionSet> transactionSet) {
        List<TransactionSetDataObject> items = new ArrayList<>();

        for(TransactionSet set:transactionSet){
            items.add(new TransactionSetDataObject(set));
        }

        dynamoRepository.save(items);
    }

    @Override
    public TransactionSet findOne(String id) {
        TransactionSetDataObject data = dynamoRepository.findOne(id);

        if(data == null){
            return  null;
        }
        return data.getTransactionSet();
    }
}
