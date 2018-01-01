package com.transactrules.accounts.services;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeServiceImpl(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository =accountTypeRepository;
    }

    @Override
    public AccountType create(AccountType accountType)  {
        //List<ApiSubError> errors = new ArrayList<>();

        AccountType existingAccountType = accountTypeRepository.findByName(accountType.getName());

        if(existingAccountType != null){
            //throw new IllegalArgumentException("accountType with same name already exists");
            //errors.add(new ApiValidationError(null, String.format("accountType %s does not exist", accountType.getName())));
        }

        //ApiValidationException.throwIfHasErrors(errors);

        if(accountType.getId()==null || accountType.getId().isEmpty()){
            accountType.setId(UUID.randomUUID().toString());
        }

        AccountType result = accountTypeRepository.save(accountType);

        return result;
    }

    @Override
    public List<AccountType> findAll() {
        Iterable<AccountType> items = accountTypeRepository.findAll();

        Iterator<AccountType> iterator = items.iterator();

        List<AccountType> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);

        return list;
    }
}
