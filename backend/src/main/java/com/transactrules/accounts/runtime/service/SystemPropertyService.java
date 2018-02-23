package com.transactrules.accounts.runtime.service;

import java.time.LocalDate;

public interface SystemPropertyService {
    LocalDate getActionDate();
    void incrementActionDate();
}
