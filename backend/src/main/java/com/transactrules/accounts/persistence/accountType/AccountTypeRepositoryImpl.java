package com.transactrules.accounts.persistence.accountType;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountTypeRepositoryImpl implements AccountTypeRepository {

    @Autowired
    JpaAccountTypeRepository dynamoRepository;

    @Override
    public void save(AccountType accountType) {
        dynamoRepository.save(new AccountTypeDataObject(accountType));
    }

    @Override
    public List<AccountType> findAll() {
        List<AccountType> list = new ArrayList<>();

        for(AccountTypeDataObject  dataObject: dynamoRepository.findAll()){
            list.add(dataObject.getAccountType());
        }

        return list;
    }

    @Override
    public AccountType findOne(String key) {
        AccountTypeDataObject dataObject=  dynamoRepository.findOne(key);

        if(dataObject==null){
            return null;
        }

        return dataObject.getAccountType();
    }

    @Override
    public void delete(String key) {
        dynamoRepository.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return dynamoRepository.exists(key);
    }
}
