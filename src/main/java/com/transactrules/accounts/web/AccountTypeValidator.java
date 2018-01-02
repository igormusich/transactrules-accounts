package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import com.transactrules.accounts.configuration.TransactionRuleType;
import com.transactrules.accounts.configuration.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class AccountTypeValidator implements Validator {
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeValidator(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountType.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountType request = (AccountType) target;

        if(hasNonEmptyName(request)){
            validateIfAccountTypeExists(errors, request);
        }

        List<TransactionType> transactionTypes = request.getTransactionTypes();

        for(int i=0; i< transactionTypes.size(); i++){
            TransactionType transactionType = transactionTypes.get(i);
            for(int ruleCount=0; ruleCount< transactionType.getTransactionRules().size(); ruleCount++){
                TransactionRuleType transactionRuleType= transactionType.getTransactionRules().get(ruleCount);

                if(isPositionTypeDefined(request, transactionRuleType.getPosititonTypeName())){
                    errors.rejectValue("transactionTypes[" + i + "].transactionRules["+ ruleCount + "].posititonTypeName", ApiErrorCode.NO_SUCH_TYPE.getCode(),  "Position type is not defined"   );
                }
            }
        }
    }

    private boolean isPositionTypeDefined(AccountType request, String positionTypeName) {
        return request.getPositionTypes().stream().noneMatch(pt->pt.getName().equals(positionTypeName));
    }

    private void validateIfAccountTypeExists(Errors errors, AccountType request) {
        AccountType existingAccountType = accountTypeRepository.findByName(request.getName());

        if (existingAccountType!=null) {
            errors.reject(ApiErrorCode.ALREADY_EXISTS.getCode());
        }
    }

    private boolean hasNonEmptyName(AccountType request) {
        return request.getName()!=null && !request.getName().isEmpty();
    }
}
