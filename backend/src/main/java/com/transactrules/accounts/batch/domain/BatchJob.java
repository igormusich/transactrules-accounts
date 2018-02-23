package com.transactrules.accounts.batch.domain;

import java.time.LocalDateTime;

public abstract class BatchJob {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    abstract void run();

}
