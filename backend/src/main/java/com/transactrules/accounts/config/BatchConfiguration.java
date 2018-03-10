package com.transactrules.accounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {
    @Value("${transactrules.batch.dayend.accountSetSize}")
    public Integer accountSetSize;

    @Value("${transactrules.batch.dayend.concurentTasks}")
    public Integer concurentTasks;
}
