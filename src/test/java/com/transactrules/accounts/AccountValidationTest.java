package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AmountValue;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.runtime.DateValue;
import com.transactrules.accounts.services.AccountService;
import com.transactrules.accounts.services.AccountTypeService;
import com.transactrules.accounts.web.AccountValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountValidationTest {

    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    private AccountValidator accountValidator;

    @Autowired
    private CodeGenService codeGenService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountService accountService;

    private AccountType loanGivenAccountType;


    @Before
    public void setup() {
        beanFactory.autowireBean(accountValidator);
        loanGivenAccountType = TestUtility.CreateLoanGivenAccountType();
        loanGivenAccountType.setClassName("LoanGivenAccountValidationTest");

        accountTypeService.save(loanGivenAccountType);
    }

    @Test
    public void testValidSuccess() {

        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        Account account = accountService.create(loanGivenAccountType);

        //add mandatory amounts and dates
        account.getDates().put("StartDate", new DateValue(LocalDate.now()));
        account.getDates().put("AccrualStart", new DateValue(LocalDate.now()));
        account.getDates().put("EndDate", new DateValue(LocalDate.now().plusYears(10)));

        account.getAmounts().put("Advance", new AmountValue(BigDecimal.valueOf(624000)));

        Errors errors = new BeanPropertyBindingResult(account, "account");
        accountValidator.validate(account, errors);

        assertThat(errors.hasErrors(), is(false));

    }

    @Test
    public void testMissingRequiredShouldFail() {

        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        Account account = accountService.create(loanGivenAccountType);


        Errors errors = new BeanPropertyBindingResult(account, "account");
        accountValidator.validate(account, errors);

        assertThat(errors.hasErrors(), is(true));

        assertThat(errors.getFieldErrors().size(), is(4));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is("StartDate is required."));
        assertThat(errors.getFieldErrors().get(1).getDefaultMessage(), is("AccrualStart is required."));
        assertThat(errors.getFieldErrors().get(2).getDefaultMessage(), is("EndDate is required."));
        assertThat(errors.getFieldErrors().get(3).getDefaultMessage(), is("AdvanceAmount is required."));

    }
}
