package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class AccountDto {
    public String accountTypeName;
    public String accountNumber;

    public List<AmountValueDto> amounts = new ArrayList<>();

    public List<PositionDto> positions = new ArrayList<>();

    public List<DateValueDto> dates = new ArrayList<>();

    private List<OptionValueDto> options = new ArrayList<>();

    private List<ScheduleDto> schedules = new ArrayList<>();

    private List<TransactionDto> transactions = new ArrayList<>();
}
