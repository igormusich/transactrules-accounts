package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.repository.UniqueIdRepository;
import com.transactrules.accounts.runtime.domain.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniqueIdServiceImpl implements UniqueIdService {
    @Autowired
    UniqueIdRepository uniqueIdRepository;

    @Override
    public String getNextId(String className) {


        //TransactionManager txManager = new TransactionManager(amazonDynamoDB, "Transaction", "Account");

        //com.amazonaws.services.dynamodbv2.transactions.Transaction tx = txManager.newTransaction();

        UniqueId uniqueId= uniqueIdRepository.findOne(className);

        String nextId = uniqueId.allocateNextId();

        uniqueIdRepository.save(uniqueId);

        return  nextId;
    }
}
