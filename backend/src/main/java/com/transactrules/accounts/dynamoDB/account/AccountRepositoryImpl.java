package com.transactrules.accounts.dynamoDB.account;

import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountRepositoryImpl implements AccountRepository {
    @Autowired
    DynamoAccountRepository dynamoRepository;

    @Override
    public void save(Account accountType) {
        dynamoRepository.save(new AccountDataObject(accountType));
    }

    @Override
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();

        for(AccountDataObject  dataObject: dynamoRepository.findAll()){
            list.add(dataObject.getAccount());
        }

        return list;
    }

    @Override
    public Account findOne(String key) {
        AccountDataObject dataObject=  dynamoRepository.findOne(key);

        if(dataObject==null){
            return null;
        }

        return dataObject.getAccount();
    }

}
