package com.transactrules.accounts;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.configuration.AccountTypeRepository;
import com.transactrules.accounts.runtime.Account;
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

import java.util.UUID;

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

        Account createAccount = new Account();
        createAccount.setAccountNumber(UUID.randomUUID().toString());
        createAccount.setAccountTypeName(accountType.getName());


        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        String createAccountJson = mapper.writeValueAsString(createAccount);

        this.mvc.perform(post("/accounts").content(createAccountJson).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.accountNumber").value(createAccount.getAccountNumber()))
                .andExpect(jsonPath("$.accountTypeName").value(createAccount.getAccountTypeName()));
    }

}
