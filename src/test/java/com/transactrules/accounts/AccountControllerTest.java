package com.transactrules.accounts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.AccountBuilder;
import com.transactrules.accounts.runtime.BusinessDayCalculator;
import com.transactrules.accounts.runtime.CodeGenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class AccountControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    private AccountType accountType;

    @Autowired
    private CodeGenService codeGenService;


    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        accountType = TestUtility.CreateLoanGivenAccountType();
        accountType.setName("loanGivenControllerTest");

        accountTypeRepository.save(accountType);

    }



    @Test
    public void givenAccountTypeandPost_whenMockMVC_thenResponseOK() throws Exception {

        Account createAccount = CreateLoanGivenAccount("AC-002-098398", LocalDate.now(),LocalDate.now().plusYears(10),null);

        ObjectMapper mapper = ObjectMapperConfiguration.getObjectMapper();
        String createAccountJson = mapper.writeValueAsString(createAccount);

        this.mvc.perform(post("/accounts").content(createAccountJson).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.accountNumber").value(createAccount.getAccountNumber()))
                .andExpect(jsonPath("$.accountTypeName").value(createAccount.getAccountTypeName()));
    }

    private Account CreateLoanGivenAccount(String accountNumber, LocalDate startDate, LocalDate endDate, BusinessDayCalculator businessDayCalculator) {

        AccountBuilder builder = new AccountBuilder(accountType, accountNumber, codeGenService );

        builder.setBusinessDayCalculator(businessDayCalculator)
                .addDateValue("StartDate", startDate)
                .addDateValue("AccrualStart",startDate)
                .addDateValue("EndDate", endDate)
                .addAmountValue("AdvanceAmount", BigDecimal.valueOf(624000), startDate)
                .addRateValue("InterestRate", BigDecimal.valueOf(3.04/100), startDate)
                .addOptionValue("AccrualOption", "365");


        return builder.getAccount();
    }

}
