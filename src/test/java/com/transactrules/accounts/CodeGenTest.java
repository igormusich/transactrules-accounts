package com.transactrules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
/**
 * Created by 313798977 on 2017/08/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeGenTest {

    //private static final String parent = "~/code/accounts/";

    //private static final CachedCompiler JCC = CompilerUtils.DEBUGGING ?
            //new CachedCompiler(new File(parent, "src/test/java"), new File(parent, "target/compiled")) :
            //CompilerUtils.CACHED_COMPILER;

    @Autowired
    private CodeGenService codeGenService;

    @Autowired
    private AccountFactory accountFactory;

    @Test
    public void TestCodeGenerationTemplate() {
        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates//account.mustache");
        try {
            mustache.execute(writer, TestUtility.CreateLoanGivenAccountType()).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String code = writer.toString();

        assertThat(false, is(code.isEmpty()));

    }

    @Test
    public void CodeGenService_test() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        AccountType loanGivenAcccountType = TestUtility.CreateLoanGivenAccountType();

        Class aClass = codeGenService.getAccountClass(loanGivenAcccountType);

        Account account1 = (Account) aClass.newInstance();

        String timestamp1 = account1.generatedAt();

        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears (25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();
 

        //this shold get the value from cache
        Class class2 = codeGenService.getAccountClass(loanGivenAcccountType);
        Account account2 = (Account) class2.newInstance();
        String timestamp2 = account2.generatedAt();

        assertThat(timestamp1, equalToIgnoringCase(timestamp2));


        //this should regenerate the code
        //codeGenService.evictAccountType(loanGivenAcccountType);
        /*codeGenService.evictAllAccountTypes();
        Class class3 = codeGenService.getAccountValuationClass(loanGivenAcccountType);
        AccountValuation valuation3 = (AccountValuation) class3.newInstance();
        String timestamp3 = valuation3.generatedAt();

        assertThat(timestamp2, not(equalToIgnoringCase(timestamp3)));*/

        YAMLFactory yf = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(yf);

        String yamlString = mapper.writeValueAsString(loanGivenAcccountType);

    }

    @Test
    public void EvaluateGenerated() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {

        AccountType loanGivenAcccountType = TestUtility.CreateLoanGivenAccountType();
        loanGivenAcccountType.setName("localLoanGiven");

        Class aClass = codeGenService.getAccountClass(loanGivenAcccountType);

        Account loanGivenValuation = (Account) aClass.newInstance();

        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears (25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();

        Account account = CreateLoanGivenAccount(loanGivenAcccountType,startDate, endDate,calendar);

    }

    private  Account CreateLoanGivenAccount(AccountType accountType, LocalDate startDate, LocalDate endDate, BusinessDayCalculator businessDayCalculator) {
        Account account = accountFactory.createAccount(accountType);

        account.getDates().get("StartDate").setDate(startDate);
        account.getDates().get("AccrualStart").setDate(startDate);
        account.getDates().get("EndDate").setDate(endDate);

        //account.Amounts.Add(new AmountValue { AmountType = "AdvanceAmount", Value = 624000 });
        //account.Rates.Add(new RateValue { RateType = "InterestRate", Value = (decimal)3.04/100, ValueDate = startDate });

        account.businessDayCalculator = businessDayCalculator;

        return account;
    }

}
