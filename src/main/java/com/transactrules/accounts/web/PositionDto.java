package com.transactrules.accounts.web;

import java.math.BigDecimal;

public class PositionDto {
    String name;
    BigDecimal amount;

    public PositionDto(String name, BigDecimal amount){
        this.name=name;
        this.amount = amount;
    }
}
