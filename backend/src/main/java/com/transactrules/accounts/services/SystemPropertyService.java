package com.transactrules.accounts.services;

import java.time.LocalDate;

public interface SystemPropertyService {
    LocalDate getActionDate();
    void incrementActionDate();
}
