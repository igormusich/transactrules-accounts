package com.transactrules.accounts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.repository.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
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

    private final String accountTypeName = "AccountType_AccountControllerTest";

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        accountType = TestConfiguration.createLoanGivenAccountType();

        String accountTypeYaml;

        accountType.setClassName(accountTypeName);

        accountTypeRepository.save(accountType);



    }



    @Test
    public void givenAccountTypeandPost_whenMockMVC_thenResponseOK() throws Exception {

        Account createAccount = TestUtility.CreateLoanGivenAccount("AC-002-098398", LocalDate.now(),LocalDate.now().plusYears(10),codeGenService);

        String yaml = ObjectMapperConfiguration.getYamlObjectMapper().writeValueAsString(createAccount);

        createAccount.setAccountTypeName(accountTypeName);

        ObjectMapper mapper = ObjectMapperConfiguration.getObjectMapper();
        String createAccountJson = mapper.writeValueAsString(createAccount);

        this.mvc.perform(post("/accounts").content(createAccountJson).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.accountNumber").value(createAccount.getAccountNumber()))
                .andExpect(jsonPath("$.accountTypeName").value(createAccount.getAccountTypeName()));
    }

    @Test
    public void givenAccountTypeAndCalculateInstalments_whenMockMVC_thenResponseOK() throws Exception {

        LocalDate startDate = LocalDate.of(2010,1,1);
        LocalDate endDate = startDate.plusYears(1);
        LocalDate firstInstalment = startDate.plusMonths(1);

        Account createAccount = TestUtility.CreateLoanGivenAccount("AC-002-098398", startDate,endDate,codeGenService);

        createAccount.getRates().get("InterestRate").setValue(BigDecimal.ZERO);
        createAccount.getAmounts().get("AdvanceAmount").setAmount(BigDecimal.valueOf(12000));

        String yaml = ObjectMapperConfiguration.getYamlObjectMapper().writeValueAsString(createAccount);

        createAccount.setAccountTypeName(accountTypeName);

        ObjectMapper mapper = ObjectMapperConfiguration.getObjectMapper();
        String createAccountJson = mapper.writeValueAsString(createAccount);


        String firstInstalmentDateString = ObjectMapperConfiguration.getObjectMapper().writeValueAsString(firstInstalment);

        firstInstalmentDateString = firstInstalmentDateString.replace("\"","");

        String firstInstalmentPath = String.format("$.instalmentSets.Redemptions.instalments['%s'].amount", firstInstalmentDateString);


        this.mvc.perform(post("/accounts/solveInstalments").content(createAccountJson).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.accountNumber").value(createAccount.getAccountNumber()))
                .andExpect(jsonPath("$.accountTypeName").value(createAccount.getAccountTypeName()))
                .andExpect(jsonPath(firstInstalmentPath).value(1000.00));
    }



}
