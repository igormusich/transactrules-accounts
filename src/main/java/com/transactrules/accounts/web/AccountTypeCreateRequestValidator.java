package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class AccountTypeCreateRequestValidator implements Validator {
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeCreateRequestValidator(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountTypeCreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountTypeCreateRequest request = (AccountTypeCreateRequest) target;

        if(hasNonEmptyName(request)){
            validateIfAccountTypeExists(errors, request);
        }

        List<TransactionTypeDto> transactionTypes = request.getTransactionTypes();

        for(int i=0; i< transactionTypes.size(); i++){
            TransactionTypeDto transactionType = transactionTypes.get(i);
            for(String creditPositionTypeName: transactionType.getCreditPositionNames()){
                if(isPositionTypeDefined(request, creditPositionTypeName)){
                    errors.rejectValue("transactionTypes[" + i + "].creditPositionNames", ApiErrorCode.NO_SUCH_TYPE.getCode(),  "Credit position type is not defined"   );
                }
            }

            for(String debitPositionTypeName:transactionType.getDebitPositionNames()){
                if(isPositionTypeDefined(request, debitPositionTypeName)){
                    errors.rejectValue("transactionTypes[" + i + "].debitPositionNames", ApiErrorCode.NO_SUCH_TYPE.getCode(),  "Credit position type is not defined"   );
                }
            }
        }
    }

    private boolean isPositionTypeDefined(AccountTypeCreateRequest request, String creditPositionTypeName) {
        return request.getPositionTypes().stream().noneMatch(pt->pt.name.equals(creditPositionTypeName));
    }

    private void validateIfAccountTypeExists(Errors errors, AccountTypeCreateRequest request) {
        AccountType existingAccountType = accountTypeRepository.findByName(request.getName());

        if (existingAccountType!=null) {
            errors.reject(ApiErrorCode.ALREADY_EXISTS.getCode());
        }
    }

    private boolean hasNonEmptyName(AccountTypeCreateRequest request) {
        return request.getName()!=null && !request.getName().isEmpty();
    }
}
