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

@Component
public class ProcessFactory {

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    AccountService accountService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    CodeGenService codeGenService;

    //String stepUrlTemplate = "/accountOpen/%s/dataSets/%s";
    String accountNumber;
    AccountType accountType;
    Account account;

    public  Process  createAccountOpenProcess(AccountOpenCreateRequest request){
        accountType = accountTypeService.findByClassName(request.accountTypeName);

        if(accountType == null){
            throw new IllegalArgumentException("AccountType " + request.accountTypeName + " does not exist" );
        }

        Account existingAccount = accountService.findByAccountNumber(request.accountNumber);

        if(existingAccount != null){
            throw new IllegalArgumentException("Account" + request.accountNumber + " already exists" );
        }

        AccountBuilder builder = new AccountBuilder(accountType, request.accountNumber, this.codeGenService);

        this.account = builder.getNewAccount();

        Process process = new Process();

        this.accountNumber =  request.accountNumber;

        process.accountNumber = request.accountNumber;

        process.calendar = createCalendarEntryDataSet();

        process.dates = createDateEntryDataSet();

        process.amounts = createAmountEntryDataSet();

        process.options = createOptionEntryDataSet();

        process.rates = createRateEntryDataSet();

        process.schedules = createScheduleEntryDataSet();

        process.instalments = createInstalmentEntryDataSet();

        return process;

    }

    private List<DateElement> createDateEntryDataSet() {
        List<DateElement> dates = new ArrayList<>();

        Integer order = 1;

        for(DateType dateType: accountType.getDateTypes() ){
            DateElement element = new DateElement();
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

    private CalendarElement createCalendarEntryDataSet() {
        Integer order = 1;
        CalendarElement element = new CalendarElement();
        element.isRequired = true;
        element.labelName = "Calendar";
        element.order = order;
        element.propertyName = "businessDayCalculator";
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

    private List<AmountElement> createAmountEntryDataSet() {
        List<AmountElement> amounts = new ArrayList<>();

        Integer order = 1;

        for(AmountType amountType: accountType.getAmountTypes() ){
            AmountElement element = new AmountElement();
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

    private List<RateElement> createRateEntryDataSet() {
        List<RateElement>  rates = new ArrayList<>();

        Integer order = 1;

        for(RateType rateType: accountType.getRateTypes() ){
            RateElement element = new RateElement();
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

    private List<OptionElement> createOptionEntryDataSet() {
        List<OptionElement>  options = new ArrayList<>();

        Integer order = 1;

        for(OptionType optionType: accountType.getOptionTypes() ){
            OptionElement element = new OptionElement();
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

    private List<ScheduleElement> createScheduleEntryDataSet() {

        List<ScheduleElement> schedules = new ArrayList<>();

        Integer order = 1;

        for(ScheduleType scheduleType: accountType.getScheduleTypes() ){
            ScheduleElement element = new ScheduleElement();
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

    private List<InstalmentElement> createInstalmentEntryDataSet() {
        List<InstalmentElement> instalmnts = new ArrayList<>();

        Integer order = 1;

        for(InstalmentType scheduleType: accountType.getInstalmentTypes() ){
            InstalmentElement element = new InstalmentElement();
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
