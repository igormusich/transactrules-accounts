package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AmountType;
import com.transactrules.accounts.metadata.DateType;
import com.transactrules.accounts.repository.AccountRepository;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AmountValue;
import com.transactrules.accounts.runtime.DateValue;
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

        if(request.getAccountNumber()!=null){
            Account existingAccount = accountRepository.findOne(request.getAccountNumber());

            if(existingAccount != null){
                errors.rejectValue("accountNumber", ApiErrorCode.ALREADY_EXISTS.getCode(), null, String.format("Account number %s already exists", request.getAccountNumber()));
            }
        }
        else
        {
            errors.rejectValue("accountNumber", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
        }

        AccountType accountType = accountTypeRepository.findOne(request.getAccountTypeName());

        if(accountType == null){
            errors.rejectValue("accountTypeName", ApiErrorCode.NO_SUCH_TYPE.getCode(), null, String.format("AccountType  %s does not exists", request.getAccountTypeName()));
        }

        validateDates(errors, request, accountType);
        validateAmounts(errors, request, accountType);

    }

    private void validateDates(Errors errors, Account request, AccountType accountType) {
        for(DateType dateType: accountType.getDateTypes()){
            if(dateType.getRequired()){
                if(!request.getDates().containsKey(dateType.getPropertyName())){
                    errors.rejectValue("dates", ApiErrorCode.REQUIRED.getCode(), dateType.getPropertyName() + " is required.");
                    continue;
                }
            }
        }

        Integer i =0;
        
        for(String key:request.getDates().keySet()){
            DateValue dateValue = request.getDates().get(key);
            
            if(dateValue.getDate() == null){
                errors.rejectValue("dates[" + i + "].date", ApiErrorCode.REQUIRED.getCode(), key + " date is required.");
                continue;
            }
        
            i++;
        }
    }

    private void validateAmounts(Errors errors, Account request, AccountType accountType) {
        for(AmountType amountType: accountType.getAmountTypes()){
            if(amountType.getRequired()){
                if(!request.getAmounts().containsKey(amountType.getPropertyName())){
                    errors.rejectValue("amounts", ApiErrorCode.REQUIRED.getCode(), amountType.getPropertyName() + " is required.");
                    continue;
                }
            }
        }

        Integer i =0;

        for(String key:request.getAmounts().keySet()){
            AmountValue amountValue = request.getAmounts().get(key);

            if(amountValue.getAmount() == null){
                errors.rejectValue("amounts[" + i + "].amount", ApiErrorCode.REQUIRED.getCode(), key + " amount is required.");
                continue;
            }

            i++;
        }
    }

    
}
