package com.transactrules.accounts.web;

import com.transactrules.accounts.runtime.Account;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class AccountDto {
    @NotBlank
    private String accountTypeName;

    @NotBlank
    private String accountNumber;

    private List<AmountValueDto> amounts = new ArrayList<>();

    private List<DateValueDto> dates = new ArrayList<>();

    private List<OptionValueDto> options = new ArrayList<>();

    private List<ScheduleDto> schedules = new ArrayList<>();


    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<AmountValueDto> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<AmountValueDto> amounts) {
        this.amounts = amounts;
    }

    public List<DateValueDto> getDates() {
        return dates;
    }

    public void setDates(List<DateValueDto> dates) {
        this.dates = dates;
    }

    public List<OptionValueDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionValueDto> options) {
        this.options = options;
    }

    public List<ScheduleDto> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDto> schedules) {
        this.schedules = schedules;
    }

    static AccountDto fromAccount(Account account){
        AccountDto dto = new AccountDto();

        dto.accountNumber = account.getAccountNumber();
        //dto.accountTypeName = accountType.getName();

        /*account.getPositions().values().stream()
                .map(p-> new PositionDto(accountType.getPositionTypeById(p.getPositionTypeId()) )) .collect(Collectors.toList());
*/
        return dto;
    }


}
