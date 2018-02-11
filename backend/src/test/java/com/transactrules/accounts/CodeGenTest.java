package com.transactrules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.runtime.CodeGenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

        Class aClass = codeGenService.getAccountClass(loanGivenAcccountType, new PrintWriter(new ByteArrayOutputStream()));

        Account account1 = (Account) aClass.newInstance();

        String timestamp1 = account1.generatedAt();

        LocalDate startDate = LocalDate.of(2013, 3, 8);
        LocalDate endDate = startDate.plusYears (25);
        Calendar calendar = TestUtility.CreateEuroZoneCalendar();
 

        //this shold get the value from cache
        Class class2 = codeGenService.getAccountClass(loanGivenAcccountType,new PrintWriter(new ByteArrayOutputStream()));
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



}
