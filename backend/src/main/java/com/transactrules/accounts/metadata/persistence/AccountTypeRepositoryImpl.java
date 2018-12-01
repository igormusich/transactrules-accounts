package com.transactrules.accounts.metadata.persistence;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.metadata.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<AccountTypeDataObject> dataObject=  dynamoRepository.findById(key);

        if(!dataObject.isPresent()){
            return null;
        }

        return dataObject.get().getAccountType();
    }

    @Override
    public void delete(String key) {
        dynamoRepository.deleteById(key);
    }

    @Override
    public boolean exists(String key) {

        return dynamoRepository.existsById(key);
    }
}
