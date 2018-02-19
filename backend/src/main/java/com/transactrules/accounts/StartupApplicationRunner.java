package com.transactrules.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationRunner implements ApplicationRunner {



    @Autowired
    private TestConfiguration testConfiguration;

    @Autowired
    private DatabaseDriver databaseDriver;


    private Logger logger = LoggerFactory.getLogger(StartupApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        databaseDriver.generateDataModel();

        testConfiguration.run();

    }

}
