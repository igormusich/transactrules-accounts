package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.*;
import com.transactrules.accounts.runtime.*;
import com.transactrules.accounts.services.AccountService;
import com.transactrules.accounts.services.AccountTypeService;
import com.transactrules.accounts.services.CalendarService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AccountFormFactory {

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    AccountService accountService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    CodeGenService codeGenService;


    AccountType accountType;
    Account account;

    public static String CalendarPropertyName = "businessDayCalculator";

    public AccountForm createAccountForm(AccountType accountType){

        this.accountType = accountType;

        String accountNumber = UUID.randomUUID().toString();

        AccountBuilder builder = new AccountBuilder(accountType, accountNumber, this.codeGenService);

        this.account = builder.getNewAccount();

        AccountForm accountForm = new AccountForm();

        accountForm.calendar = createCalendarEntryDataSet();

        accountForm.dates = createDateEntryDataSet();

        accountForm.amounts = createAmountEntryDataSet();

        accountForm.options = createOptionEntryDataSet();

        accountForm.rates = createRateEntryDataSet();

        accountForm.schedules = createScheduleEntryDataSet();

        accountForm.instalments = createInstalmentEntryDataSet();

        return accountForm;

    }

    private List<DateControl> createDateEntryDataSet() {
        List<DateControl> dates = new ArrayList<>();

        Integer order = 1;

        for(DateType dateType: accountType.getDateTypes() ){
            DateControl element = new DateControl();
            element.isRequired = true;
            element.labelName = dateType.getLabelName();
            element.order = order;
            element.propertyName = dateType.getPropertyName();
            element.value = null;

            dates.add(element);

            order +=1;
        }

        return dates;

    }

    private CalendarControl createCalendarEntryDataSet() {
        Integer order = 1;
        CalendarControl element = new CalendarControl();
        element.isRequired = true;
        element.labelName = "Calendar";
        element.order = order;
        element.propertyName = CalendarPropertyName;
        element.value = null;

        element.calendarNames = getCalendarNames();

        return element;
    }

    @NotNull
    private String[] getCalendarNames() {
        ArrayList<String> calendarNamesList = new ArrayList<>();

        for(Calendar calendar: calendarService.findAll())
        {
            calendarNamesList.add(calendar.getName());
        }

        String[] calendarNames = new String[calendarNamesList.size()];

        calendarNamesList.toArray(calendarNames);
        return calendarNames;
    }

    private List<AmountControl> createAmountEntryDataSet() {
        List<AmountControl> amounts = new ArrayList<>();

        Integer order = 1;

        for(AmountType amountType: accountType.getAmountTypes() ){
            AmountControl element = new AmountControl();
            element.isRequired = true;
            element.labelName = amountType.getLabelName();
            element.order = order;
            element.propertyName = amountType.getPropertyName();
            element.value = null;

            amounts.add(element);

            order +=1;
        }

        return amounts;
    }

    private List<RateControl> createRateEntryDataSet() {
        List<RateControl>  rates = new ArrayList<>();

        Integer order = 1;

        for(RateType rateType: accountType.getRateTypes() ){
            RateControl element = new RateControl();
            element.isRequired = true;
            element.labelName = rateType.getLabelName();
            element.order = order;
            element.propertyName = rateType.getPropertyName();
            element.value = null;

            rates.add(element);

            order +=1;
        }

        return rates;

    }

    private List<OptionControl> createOptionEntryDataSet() {
        List<OptionControl>  options = new ArrayList<>();

        Integer order = 1;

        for(OptionType optionType: accountType.getOptionTypes() ){
            OptionControl element = new OptionControl();
            element.isRequired = true;
            element.labelName = optionType.getLabelName();
            element.order = order;
            element.propertyName = optionType.getPropertyName();
            element.value = this.account.getOptions().get(optionType.getPropertyName());

            options.add(element);

            order +=1;
        }

        return options;

    }

    private List<ScheduleControl> createScheduleEntryDataSet() {

        List<ScheduleControl> schedules = new ArrayList<>();

        Integer order = 1;

        for(ScheduleType scheduleType: accountType.getScheduleTypes() ){
            ScheduleControl element = new ScheduleControl();
            element.isRequired = true;
            element.labelName = scheduleType.getLabelName();
            element.order = order;
            element.propertyName = scheduleType.getPropertyName();
            element.value = new Schedule();

            schedules.add(element);

            order +=1;
        }

        return schedules;
    }

    private List<InstalmentControl> createInstalmentEntryDataSet() {
        List<InstalmentControl> instalmnts = new ArrayList<>();

        Integer order = 1;

        for(InstalmentType scheduleType: accountType.getInstalmentTypes() ){
            InstalmentControl element = new InstalmentControl();
            element.isRequired = true;
            element.labelName = scheduleType.getLabelName();
            element.order = order;
            element.propertyName = scheduleType.getPropertyName();
            element.value = new InstalmentSet();

            instalmnts.add(element);

            order +=1;
        }

        return instalmnts;
    }

}
