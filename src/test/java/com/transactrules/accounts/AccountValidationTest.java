package com.transactrules.accounts;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.AccountBuilder;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.services.AccountTypeService;
import com.transactrules.accounts.web.AccountValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountValidationTest {

    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    private AccountValidator accountValidator;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private CodeGenService codeGenService;

    private AccountType loanGivenAccountType;


    @Before
    public void setup() {
        beanFactory.autowireBean(accountValidator);
        loanGivenAccountType = accountTypeService.findByClassName("LoanGiven");
    }

    @Test
    public void testValidSuccess() {

        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        AccountBuilder builder = new AccountBuilder( loanGivenAccountType, "",codeGenService);

        //Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        //accountTypeCreateValidator.validate(accountType, errors);

        //assertThat(errors.hasErrors(), is(false));
    }
}
