package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {
    private AccountTypeRepository accountTypeRepository;
    private AccountRepository accountRepository;

    @Autowired
    public AccountValidator(
            AccountTypeRepository accountTypeRepository,
            AccountRepository accountRepository) {
        this.accountTypeRepository = accountTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account request = (Account) target;

        Account existingAccount = accountRepository.findOne(request.getAccountNumber());

        if(existingAccount != null){
            errors.rejectValue("accountNumber", ApiErrorCode.ALREADY_EXISTS.getCode(), null, String.format("Account number %s already exists", request.getAccountNumber()));
        }

        AccountType accountType = accountTypeRepository.findOne(request.getAccountTypeName());

        if(accountType == null){
            errors.rejectValue("accountTypeName", ApiErrorCode.NO_SUCH_TYPE.getCode(), null, String.format("AccountType  %s does not exists", request.getAccountTypeName()));
        }

    }
}
