package com.transactrules.accounts;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.web.ApiErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTypeValidationTest {

    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    private AccountTypeValidator accountTypeCreateValidator = new AccountTypeValidator();

    @Before
    public void setup() {
        beanFactory.autowireBean(accountTypeCreateValidator);
    }

    @Test
    public void testValidNameSuccess() {

        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        AccountType accountType = new AccountType();

        accountType.setClassName("testValidNameSuccess");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(false));
    }

    @Test
    public void testLoanGivenSuccess() {

        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testLoanGivenSuccess");


        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(false));
    }

    @Test
    public void testInvalidNameFail() {
        // I'd propertyName the test to something like
        // invalidEmailShouldFailValidation()

        AccountType accountType = new AccountType();

        accountType.setClassName(" & some ^ text");

        accountType.addPositionType("valid_position_type");
        accountType.addTransactionType("valid_transaction_type");
        accountType.getScheduleTypes().add(new ScheduleType("valid_schedule_type", ScheduleFrequency.Daily, ScheduleEndType.NoEnd, BusinessDayCalculation.AnyDay, "LocalDate.now()", "LocalDate.now().plusYears(5)",null,"1",false));

        accountType.addPositionType(" more & errors");
        accountType.addDateType(" * some more");
        accountType.addInstalmentType(" &* fails", ScheduledTransactionTiming.StartOfDay,"valid_schedule_type","valid_transaction_type", "valid_position_type",null , null);
        accountType.addTransactionType(" *&@ fail");
        accountType.addAmountType(" &!*( fail", false);
        accountType.addRateType(" *&@# (#*");
        accountType.addOptionType(" 3@3#&@* ","[\"a\"]");


        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(8));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(1).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(2).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(3).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(4).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(5).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(6).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));
        assertThat(errors.getFieldErrors().get(7).getDefaultMessage(), is(ApiErrorCode.INVALID_IDENTIFIER.getDescription()));

    }

    @Test
    public void testSingleExpressionFailsToCompile(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();

        accountType.setClassName("testSingleExpressionFailsToCompile");

        ScheduleType scheduleType= accountType.getScheduleTypes().get(0);

        scheduleType.setStartDateExpression(" @ nonsense syntax");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.getGlobalErrors().get(0).getCode(), is(ApiErrorCode.EVALUATION_FAILED.getCode()));

    }

    @Test
    public void testInvalidPositionTypeOnTransactionRule(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();

        accountType.setClassName("testInvalidPositionTypeOnTransactionRule");

        TransactionType transactionType = accountType.getTransactionTypes().get(0);

        transactionType.getTransactionRules().get(0).setPositionTypeName("invalidName");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(1));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
    }

    @Test
    public void testInvalidTransactionOperationOnTransactionRule(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testInvalidTransactionOperationOnTransactionRule");

        TransactionType transactionType = accountType.getTransactionTypes().get(0);

        transactionType.getTransactionRules().get(0).setTransactionOperation("invalid");

        TransactionOperation operation = TransactionOperation.fromString("invalid");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(1));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));
    }

    @Test
    public void testInvalidEnumOnInstalmentType(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testInvalidEnumOnInstalmentType");

        InstalmentType instalmentType = accountType.getInstalmentTypes().get(0);

        instalmentType.setTiming("invalid timing");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(1));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));
    }

    @Test
    public void testInvalidEnumOnScheduledTransaction(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();

        accountType.setClassName("testInvalidEnumOnScheduledTransaction");

        ScheduledTransaction scheduledTransaction = accountType.getScheduledTransactions().get(0);

        scheduledTransaction.setTiming("invalid timing");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(1));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));
    }

    @Test
    public void testInvalidEnumsOnScheduleType(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testInvalidEnumsOnScheduleType");

        ScheduleType scheduleType = accountType.getScheduleTypes().get(0);

        scheduleType.setBusinessDayCalculation("invalid_calculation");
        scheduleType.setScheduleFrequency("inlaid_frequency");
        scheduleType.setScheduleEndType("invalid end types");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(3));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));
        assertThat(errors.getFieldErrors().get(1).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));
        assertThat(errors.getFieldErrors().get(2).getDefaultMessage(), is(ApiErrorCode.INVALID_ENUM.getDescription()));

    }

    @Test
    public void testInvalidReferencesOnScheduledTransaction(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testInvalidReferencesOnScheduledTransaction");

        ScheduledTransaction item = accountType.getScheduledTransactions().get(0);

        item.setDateTypeName("invalid date");
        item.setScheduleTypeName("invalid schedule");
        item.setTransactionTypeName("invalid tt");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(3));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(1).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(2).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
    }

    @Test
    public void testInvalidReferencesOnInstalmentType(){

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setClassName("testInvalidReferencesOnInstalmentType");

        InstalmentType item = accountType.getInstalmentTypes().get(0);

        item.setScheduleTypeName("invalid schedule");
        item.setTransactionTypeName("invalid tt");
        item.setPositionTypeName("invalid pt");
        item.setInterestAccruedPositionTypeName("another invalid");
        item.setInterestCapitalizedPositionTypeName(" and another one");

        Errors errors = new BeanPropertyBindingResult(accountType, "accountType");
        accountTypeCreateValidator.validate(accountType, errors);

        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldErrors().size(), is(5));
        assertThat(errors.getFieldErrors().get(0).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(1).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(2).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(3).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
        assertThat(errors.getFieldErrors().get(4).getDefaultMessage(), is(ApiErrorCode.NO_SUCH_TYPE.getDescription()));
    }
}
