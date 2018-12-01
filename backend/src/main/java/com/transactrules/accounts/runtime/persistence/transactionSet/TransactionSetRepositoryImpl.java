package com.transactrules.accounts.runtime.persistence.transactionSet;

import com.transactrules.accounts.runtime.repository.TransactionSetRepository;
import com.transactrules.accounts.runtime.domain.TransactionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionSetRepositoryImpl implements TransactionSetRepository {

    @Autowired
    JpaTransactionSetRepository dynamoRepository;

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
        dynamoRepository.saveAll(items);
    }

    @Override
    public TransactionSet findOne(String id) {
        Optional<TransactionSetDataObject> data = dynamoRepository.findById(id);

        if(!data.isPresent()){
            return  null;
        }
        return data.get().getTransactionSet();
    }
}
