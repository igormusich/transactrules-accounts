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


        String json = "{\n" +
                "  \"className\": \"SimpleLoan\",\n" +
                "  \"labelName\": \"Simple Loan\",\n" +
                "  \"amountTypes\": [\n" +
                "    {\n" +
                "      \"isValueDated\": false,\n" +
                "      \"propertyName\": \"LoanAmount\",\n" +
                "      \"labelName\":\"Loan Amount\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"dateTypes\": [\n" +
                "    {\n" +
                "      \"propertyName\": \"StartDate\",\n" +
                "      \"labelName\":\"Start Date\",\n" +
                "      \"isRequired\":true,\n" +
                "      \"isStartDate\":true\n" +
                "    },\n" +
                "    {\n" +
                "      \"propertyName\": \"EndDate\",\n" +
                "      \"labelName\":\"End Date\",\n" +
                "      \"isRequired\":true,\n" +
                "      \"isStartDate\":false\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"positionTypes\": [\n" +
                "    {\n" +
                "      \"propertyName\": \"Current\",\n" +
                "      \"labelName\":\"Current Balance\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"propertyName\": \"InterestAccrued\",\n" +
                "      \"labelName\":\"Interest Accrued Balance\"\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"transactionTypes\": [\n" +
                "    {\n" +
                "      \"propertyName\": \"Deposit\",\n" +
                "      \"labelName\":\"Deposit\",\n" +
                "      \"creditPositionNames\": [\n" +
                "        \"Current\"\n" +
                "      ],\n" +
                "      \"maximumPrecision\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"propertyName\": \"Withdrawal\",\n" +
                "      \"labelName\":\"Withdrawal\",\n" +
                "      \"debitPositionNames\": [\n" +
                "        \"Current\"\n" +
                "      ],\n" +
                "      \"maximumPrecision\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"propertyName\": \"InterestCapitalized\",\n" +
                "      \"labelName\":\"Interest Capitalization\",\n" +
                "      \"creditPositionNames\": [\n" +
                "        \"Current\"\n" +
                "      ],\n" +
                "      \"debitPositionNames\": [\n" +
                "        \"InterestAccrued\"\n" +
                "      ],\n" +
                "      \"maximumPrecision\": false\n" +
                "    }\n" +
                "  ]\n" +
                "}";


        this.mvc.perform(post("/accountTypes").content(json).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.className").value("SimpleLoan"));
    }

    @Test
    public void givenLoanGivenYmlUrlandPost_whenMockMVC_thenResponseOK() throws Exception {

        AccountType accountType = TestConfiguration.createLoanGivenAccountType();
        accountType.setClassName("LoanGiven_AccountTypeController");
        ObjectMapper objectMapper = ObjectMapperConfiguration.getYamlObjectMapper();

        String createAccountTypeYml = objectMapper.writeValueAsString(accountType);

        this.mvc.perform(post("/accountTypes").content(createAccountTypeYml).contentType("text/yml")).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.className").value("LoanGiven_AccountTypeController"));
    }

}
