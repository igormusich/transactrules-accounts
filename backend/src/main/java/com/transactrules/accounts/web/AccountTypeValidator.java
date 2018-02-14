package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.lang.model.SourceVersion;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

@Component
public class AccountTypeValidator implements Validator {

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    AccountTypeRepository accountTypeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountType.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountType accountType = (AccountType) target;

        if (!Utility.isEmpty(accountType.getClassName()) && !SourceVersion.isName(accountType.getClassName())){
            errors.rejectValue("className", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
        }

        HashSet<String> names = new HashSet<>();

        validateIfAccountTypeExists(errors, accountType);

        validatePositionTypes(errors, accountType, names);

        validateTransactionTypes(errors, accountType, names);

        validateDateTypes(errors, accountType, names);

        validateAmountTypes(errors, accountType, names);

        validateOptionTypes(errors, accountType, names);

        validateRateTypes(errors, accountType, names);

        validateScheduleTypes(errors, accountType, names);

        validateInstalmentTypes(errors, accountType, names);

        validateScheduledTransactions(errors,accountType);


        //do not add compilation errors in situations where we can be more specific
        if(errors.getErrorCount()==0){
            evaluateCompiledClass(errors, accountType);
        }

    }

    private void validateIfAccountTypeExists(Errors errors, AccountType request) {

        if(Utility.isEmpty(request.getClassName()))
        {
            //this should be caught by @NotBlank validation, however we can't search by blank name
            return;
        }

        AccountType existingAccountType = accountTypeRepository.findOne(request.getClassName());

        if (existingAccountType!=null) {
            errors.rejectValue("className", ApiErrorCode.ALREADY_EXISTS.getCode());
        }
    }


    private void evaluateCompiledClass(Errors errors, AccountType accountType) {

        try {

            Class accountClass =  codeGenService.getAccountClass(accountType);
            Account account = (Account) accountClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String aString = "";
            
            errors.reject(ApiErrorCode.EVALUATION_FAILED.getCode(), e.getMessage() + aString);
        }
    }


    private void validateScheduledTransactions(Errors errors,AccountType accountType){
        for(int i=0; i < accountType.getScheduledTransactions().size(); i++){
            ScheduledTransaction scheduledTransaction = accountType.getScheduledTransactions().get(i);

            if(accountType.getTransactionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(scheduledTransaction.getTransactionTypeName())))
            {
                errors.rejectValue("scheduledTransactions[" + i + "].transactionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(Utility.isEmpty(scheduledTransaction.getScheduleTypeName())&& Utility.isEmpty(scheduledTransaction.getDateTypeName())){
                errors.rejectValue("scheduledTransactions[" + i + "].scheduleTypeName","missing", "Either schedule or date type need to be provided for ScheduledTransaction");
            }

            if(!Utility.isEmpty(scheduledTransaction.getScheduleTypeName()) &&
                    accountType.getScheduleTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(scheduledTransaction.getScheduleTypeName()))){
                errors.rejectValue("scheduledTransactions[" + i + "].scheduleTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(!Utility.isEmpty(scheduledTransaction.getDateTypeName()) &&
                    accountType.getDateTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(scheduledTransaction.getDateTypeName()))){
                errors.rejectValue("scheduledTransactions[" + i + "].dateTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(ScheduledTransactionTiming.fromString(scheduledTransaction.getTiming())==null){
                errors.rejectValue("scheduledTransactions[" + i + "].timing", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
            }

        }
    }

    private void validatePositionTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getPositionTypes().size(); i++){
            PositionType positionType = accountType.getPositionTypes().get(i);

            String name = positionType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("positionTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("positionTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("positionTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);
        }
    }

    private void validateTransactionTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getTransactionTypes().size(); i++){
            TransactionType transactionType = accountType.getTransactionTypes().get(i);

            String name = transactionType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("transactionTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("transactionTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            for(int j=0; j < transactionType.getTransactionRules().size(); j++){
                TransactionRuleType rule = transactionType.getTransactionRules().get(j);

                if(!Utility.isEmpty(rule.getPositionTypeName()) &&
                        accountType.getPositionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(rule.getPositionTypeName()))){
                    errors.rejectValue("transactionTypes[" + i + "].transactionRules[" + j + "].positionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
                }


                if(TransactionOperation.fromString(rule.getTransactionOperation())==null){
                    errors.rejectValue("transactionTypes[" + i + "].transactionRules[" + j + "].transactionOperation", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
                }

            }

        }
    }

    private void validateAmountTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getAmountTypes().size(); i++){
            AmountType amountType = accountType.getAmountTypes().get(i);

            String name = amountType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("amountTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("amountTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("amountTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);
        }
    }

    private void validateDateTypes(Errors errors, AccountType accountType, HashSet<String> names) {

        Boolean hasStartDate = false;

        for(int i=0; i < accountType.getDateTypes().size(); i++){
            DateType dateType = accountType.getDateTypes().get(i);

            String name = dateType.getPropertyName();

            if(dateType.getIsStartDate()){
                if(hasStartDate){
                    errors.rejectValue("dateTypes["+ i +"].propertyName", ApiErrorCode.HAS_DUPLICATE_START_DATE.getCode(), ApiErrorCode.HAS_DUPLICATE_START_DATE.getDescription());
                    continue;
                }
                else {
                    hasStartDate = true;
                }
            }

            if(Utility.isEmpty(name)){
                errors.rejectValue("dateTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("dateTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("dateTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);
        }
    }

    private void validateRateTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getRateTypes().size(); i++){
            RateType rateType = accountType.getRateTypes().get(i);

            String name = rateType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("rateTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("rateTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("rateTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);
        }
    }

    private void validateOptionTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getOptionTypes().size(); i++){
            OptionType optionType = accountType.getOptionTypes().get(i);

            String name = optionType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("optionTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("optionTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("optionTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode() , ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);
        }
    }

    private void validateScheduleTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getScheduleTypes().size(); i++){
            ScheduleType scheduleType = accountType.getScheduleTypes().get(i);

            String name = scheduleType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("scheduleTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("scheduleTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("scheduleTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            if(BusinessDayCalculation.fromString(scheduleType.getBusinessDayCalculation())==null){
                errors.rejectValue("scheduleTypes["+ i +"].businessDayCalculation", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
            }

            if(ScheduleFrequency.fromString(scheduleType.getScheduleFrequency())==null){
                errors.rejectValue("scheduleTypes["+ i +"].scheduleFrequency", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
            }
            if(ScheduleEndType.fromString(scheduleType.getScheduleEndType())==null){
                errors.rejectValue("scheduleTypes["+ i +"].scheduleEndType", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
            }
            names.add(name);
        }
    }

    private void validateInstalmentTypes(Errors errors, AccountType accountType, HashSet<String> names) {
        for(int i=0; i < accountType.getInstalmentTypes().size(); i++){
            InstalmentType instalmentType = accountType.getInstalmentTypes().get(i);

            String name = instalmentType.getPropertyName();

            if(Utility.isEmpty(name)){
                errors.rejectValue("instalmentTypes["+ i +"].propertyName", ApiErrorCode.REQUIRED.getCode(), ApiErrorCode.REQUIRED.getDescription());
                continue;
            }

            if(!SourceVersion.isName(name)){
                errors.rejectValue("instalmentTypes["+ i +"].propertyName", ApiErrorCode.INVALID_IDENTIFIER.getCode(), ApiErrorCode.INVALID_IDENTIFIER.getDescription());
            }

            if(names.contains(name)){
                errors.rejectValue("instalmentTypes["+ i +"].propertyName", ApiErrorCode.ALREADY_EXISTS.getCode(), ApiErrorCode.ALREADY_EXISTS.getDescription());
            }

            names.add(name);

            if(!Utility.isEmpty(instalmentType.getScheduleTypeName()) &&
                    accountType.getScheduleTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(instalmentType.getScheduleTypeName()))){
                errors.rejectValue("instalmentTypes[" + i + "].scheduleTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(!Utility.isEmpty(instalmentType.getTransactionTypeName()) &&
                    accountType.getTransactionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(instalmentType.getTransactionTypeName()))){
                errors.rejectValue("instalmentTypes[" + i + "].transactionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(!Utility.isEmpty(instalmentType.getPositionTypeName()) &&
                    accountType.getPositionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(instalmentType.getPositionTypeName()))){
                errors.rejectValue("instalmentTypes[" + i + "].positionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(!Utility.isEmpty(instalmentType.getInterestAccruedPositionTypeName()) &&
                    accountType.getPositionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(instalmentType.getInterestAccruedPositionTypeName()))){
                errors.rejectValue("instalmentTypes[" + i + "].interestAccruedPositionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(!Utility.isEmpty(instalmentType.getInterestCapitalizedPositionTypeName()) &&
                    accountType.getPositionTypes().stream().noneMatch(tt-> tt.getPropertyName().equals(instalmentType.getInterestCapitalizedPositionTypeName()))){
                errors.rejectValue("instalmentTypes[" + i + "].interestCapitalizedPositionTypeName",ApiErrorCode.NO_SUCH_TYPE.getCode(), ApiErrorCode.NO_SUCH_TYPE.getDescription() );
            }

            if(ScheduledTransactionTiming.fromString(instalmentType.getTiming())==null){
                errors.rejectValue("instalmentTypes[" + i + "].timing", ApiErrorCode.INVALID_ENUM.getCode(), ApiErrorCode.INVALID_ENUM.getDescription());
            }

        }
    }


}


