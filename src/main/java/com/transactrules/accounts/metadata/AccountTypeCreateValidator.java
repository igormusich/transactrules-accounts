package com.transactrules.accounts.metadata;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccountTypeCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountType.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountType accountType = (AccountType) target;

        if (checkInputString(accountType.getName())) {
            errors.rejectValue("name", "name.empty");
        }


    }

    private boolean checkInputString(String input) {
        return (input == null || input.isEmpty() );
    }
}


/*
public class WebsiteUserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return WebsiteUser.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        WebsiteUser user = (WebsiteUser) obj;
        if (checkInputString(user.getName())) {
            errors.rejectValue("name", "name.empty");
        }

        if (checkInputString(user.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}*/
