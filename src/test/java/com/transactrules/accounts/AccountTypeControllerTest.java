package com.transactrules.accounts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class AccountTypeControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }



    @Test
    public void givenSimpleLoanUrlandPost_whenMockMVC_thenResponseOK() throws Exception {

        java.net.URL url = getClass().getResource("/SimpleLoan.json");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String createAccountJson = new String(java.nio.file.Files.readAllBytes(resPath));

        this.mvc.perform(post("/accountTypes").content(createAccountJson).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").value("SimpleLoan"));
    }

    @Test
    public void givenLoanGivenYmlUrlandPost_whenMockMVC_thenResponseOK() throws Exception {

        AccountType accountType = TestUtility.CreateLoanGivenAccountType();
        ObjectMapper objectMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        String createAccountTypeYml = objectMapper.writeValueAsString(accountType);

        this.mvc.perform(post("/accountTypes").content(createAccountTypeYml).contentType("text/yml")).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.name").value("LoanGiven"));
    }

}
