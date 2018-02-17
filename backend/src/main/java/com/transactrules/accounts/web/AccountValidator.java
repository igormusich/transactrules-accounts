package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.repository.AccountRepository;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.repository.CalendarRepository;
import com.transactrules.accounts.runtime.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {
    private AccountTypeRepository accountTypeRepository;
    private AccountRepository accountRepository;
    private CalendarRepository calendarRepository;

    @Autowired
    public AccountValidator(
            AccountTypeRepository accountTypeRepository,
            AccountRepository accountRepository,
            CalendarRepository calendarRepository) {

        this.accountTypeRepository = accountTypeRepository;
        this.accountRepository = accountRepository;
        this.calendarRepository = calendarRepository;
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

        if(request.getCalendarNames().size()==0){
            errors.rejectValue("calendarNames", ApiErrorCode.REQUIRED.getCode(), "At least one calendar name is required.");
        }
        else{
            Integer i =0;
            for(String calendarName:request.getCalendarNames()){
                Calendar calendar = calendarRepository.findOne(calendarName);

                if(calendar == null){
                    errors.rejectValue("calendarNames[" + i + "]", ApiErrorCode.NO_SUCH_TYPE.getCode(), null, String.format("Calendar  %s does not exists", calendarName));
                }

                i++;
            }
        }

        if(errors.hasErrors()){
            //terminate validation not having account number or account type
            return;
        }

        validateDates(errors, request, accountType);
        validateAmounts(errors, request, accountType);
        validateRates(errors, request, accountType);
        validateOptions(errors, request, accountType);

    }

    private void validateDates(Errors errors, Account request, AccountType accountType) {
        for(DateType dateType: accountType.getDateTypes()){
            if(dateType.getRequired()){
                if(!request.getDates().containsKey(dateType.getPropertyName())){
                    errors.rejectValue("dates", ApiErrorCode.REQUIRED.getCode(), dateType.getPropertyName() + " is required.");
                }
            }
        }

        Integer i =0;
        
        for(String key:request.getDates().keySet()){
            DateValue dateValue = request.getDates().get(key);
            
            if(dateValue.getDate() == null){
                errors.rejectValue("dates['" + key + "'].date", ApiErrorCode.REQUIRED.getCode(), key + " date is required.");
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

    private void validateRates(Errors errors, Account request, AccountType accountType) {
        for(RateType rateType: accountType.getRateTypes()){
            if(rateType.getRequired()){
                if(!request.getRates().containsKey(rateType.getPropertyName())){
                    errors.rejectValue("rates", ApiErrorCode.REQUIRED.getCode(), rateType.getPropertyName() + " is required.");
                    continue;
                }
            }
        }

        Integer i =0;

        for(String key:request.getRates().keySet()){
            RateValue rateValue = request.getRates().get(key);

            if(rateValue.getValue() == null){
                errors.rejectValue("rates[" + i + "].rate", ApiErrorCode.REQUIRED.getCode(), key + " rate is required.");
                continue;
            }

            i++;
        }
    }

    private void validateOptions(Errors errors, Account request, AccountType accountType) {
        for(OptionType optionType: accountType.getOptionTypes()){
            if(optionType.getRequired()){
                if(!request.getOptions().containsKey(optionType.getPropertyName())){
                    errors.rejectValue("options", ApiErrorCode.REQUIRED.getCode(), optionType.getPropertyName() + " is required.");
                    continue;
                }
            }
        }

        Integer i =0;

        for(String key:request.getOptions().keySet()){
            OptionValue optionValue = request.getOptions().get(key);

            if(optionValue.getValue() == null){
                errors.rejectValue("options[" + i + "].value", ApiErrorCode.REQUIRED.getCode(), key + " is required.");
                continue;
            }

            i++;
        }
    }

    
}
