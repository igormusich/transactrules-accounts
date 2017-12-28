package com.transactrules.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.runtime.BusinessDayCalculator;
import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.runtime.DateValue;
import com.transactrules.accounts.runtime.accounts.Account;
import com.transactrules.accounts.runtime.accounts.AccountValuation;
import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;

/**
 * Created by 313798977 on 2017/08/11.
 */
public class CodeGenTest {

    private static final String parent = "~/code/accounts/";

    private static final CachedCompiler JCC = CompilerUtils.DEBUGGING ?
            new CachedCompiler(new File(parent, "src/test/java"), new File(parent, "target/compiled")) :
            CompilerUtils.CACHED_COMPILER;

    @Test
    public void TestCodeGenerationTemplate() {
        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates//accountValuation.mustache");
        try {
            mustache.execute(writer, TestUtility.CreateLoanGivenAccountType()).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String code = writer.toString();

        assertThat(false, is(code.isEmpty()));

    }

    @Test
    public void EvaluateGenerated() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, JsonProcessingException {

        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates//accountValuation.mustache");
        AccountType loanGivenAcccountType = TestUtility.CreateLoanGivenAccountType();

        try {
            mustache.execute(writer,loanGivenAcccountType).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String className = "com.transactrules.accounts.runtime.accounts." + loanGivenAcccountType.getName() + "Valuation";

        Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, writer.toString());
        AccountValuation loanGivenValuation = (AccountValuation) aClass.newInstance();

        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears (25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();

        Account account = CreateLoanGivenAccount(loanGivenAcccountType,startDate, endDate,calendar);

        loanGivenValuation.initialize(account, loanGivenAcccountType);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String yaml = null;

        yaml= mapper.writeValueAsString(account).trim();

    }

    private  Account CreateLoanGivenAccount(AccountType accountType, LocalDate startDate, LocalDate endDate, BusinessDayCalculator businessDayCalculator) {
        Account account = new Account();

        account.initializeDate(accountType.getDateTypeByName("StartDate").get(),startDate);
        account.initializeDate(accountType.getDateTypeByName("AccrualStart").get(),startDate);
        account.initializeDate(accountType.getDateTypeByName("EndDate").get(),endDate);


        account.initialize(accountType);


        //account.Amounts.Add(new AmountValue { AmountType = "AdvanceAmount", Value = 624000 });
        //account.Rates.Add(new RateValue { RateType = "InterestRate", Value = (decimal)3.04/100, ValueDate = startDate });

        account.businessDayCalculator = businessDayCalculator;

        return account;
    }

}
