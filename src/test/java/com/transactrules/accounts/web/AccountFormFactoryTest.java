package com.transactrules.accounts.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.transactrules.accounts.TestUtility;
import com.transactrules.accounts.config.ObjectMapperConfiguration;
import com.transactrules.accounts.metadata.AccountType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountFormFactoryTest {

    @Autowired
    AccountFormFactory factory;

    @Test
    public void createAccountOpenProcess() throws JsonProcessingException {
        AccountType accountType = TestUtility.CreateLoanGivenAccountType();

         AccountForm accountForm = factory.createAccountForm(accountType);

        ObjectMapper mapper = ObjectMapperConfiguration.getYamlObjectMapper();

        //ObjectMapper mapper =ObjectMapperConfiguration.getObjectMapper();

        ObjectWriter writer = mapper.writer();

        String output = writer.writeValueAsString(accountForm);

    }
}