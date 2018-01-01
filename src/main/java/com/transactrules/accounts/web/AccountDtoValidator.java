package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountDtoValidator implements Validator {
    private AccountTypeRepository accountTypeRepository;
    private AccountRepository accountRepository;

    @Autowired
    public AccountDtoValidator(
            AccountTypeRepository accountTypeRepository,
            AccountRepository accountRepository) {
        this.accountTypeRepository = accountTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDto request = (AccountDto) target;

        Account existingAccount = accountRepository.findOne(request.getAccountNumber());

        if(existingAccount != null){
            errors.rejectValue("accountNumber", ApiErrorCode.ALREADY_EXISTS.getCode(), null, String.format("Account number %s already exists", request.getAccountNumber()));
        }

        AccountType accountType = accountTypeRepository.findByName(request.getAccountTypeName());

        if(accountType == null){
            errors.rejectValue("accountTypeName", ApiErrorCode.NO_SUCH_TYPE.getCode(), null, String.format("AccountType  %s does not exists", request.getAccountTypeName()));
        }

    }
}
