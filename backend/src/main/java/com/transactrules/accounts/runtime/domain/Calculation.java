package com.transactrules.accounts.runtime.domain;

import java.math.BigDecimal;

public interface Calculation {
    BigDecimal calculate(BigDecimal amount);
}
