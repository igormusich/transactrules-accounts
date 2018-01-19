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

    String stepUrlTemplate = "/accountOpen/%s/dataSets/%s";
    String processId ;
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

        this.processId = "AO-" + request.accountNumber;

        process.processId = "AO-" + request.accountNumber;
        process.status = ProcessStatus.Pending;

        Integer stepId = 1;

        process.calendarSet = createCalendarEntryDataSet(stepId);

        stepId +=1;

        process.dateSet = createDateEntryDataSet(stepId);

        stepId +=1;
        
        process.amountSet = createAmountEntryDataSet(stepId);

        stepId +=1;

        process.optionSet = createOptionEntryDataSet(stepId);

        stepId +=1;

        process.rateSet = createRateEntryDataSet(stepId);

        stepId +=1;
        
        Integer[] scheduleDependsOn = {process.calendarSet.dataSetId, process.dateSet.dataSetId};
        
        process.scheduleSet = createScheduleEntryDataSet(stepId, scheduleDependsOn);

        stepId +=1;

        Integer[] instalmentDependsOn = {process.calendarSet.dataSetId, process.dateSet.dataSetId, process.amountSet.dataSetId, process.optionSet.dataSetId, process.rateSet.dataSetId, process.scheduleSet.dataSetId };

        process.instalmentSet = createInstalmentEntryDataSet(stepId, instalmentDependsOn);

        return process;

    }

    private DateSet createDateEntryDataSet(Integer stepId) {
        DateSet dataSet = new DateSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Dates";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );

        Integer order = 1;

        for(DateType dateType: accountType.getDateTypes() ){
            DateElement element = new DateElement();
            element.isRequired = true;
            element.labelName = dateType.getLabelName();
            element.order = order;
            element.propertyName = dateType.getPropertyName();
            element.value = null;

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;

    }

    private CalendarSet createCalendarEntryDataSet(Integer stepId) {
        CalendarSet dataSet = new CalendarSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Calendars";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );

        Integer order = 1;

        CalendarElement element = new CalendarElement();
        element.isRequired = true;
        element.labelName = "Calendar";
        element.order = order;
        element.propertyName = "businessDayCalculator";
        element.value = null;

        element.calendarNames = getCalendarNames();

        dataSet.data = element;

        return dataSet;

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

    private AmountSet createAmountEntryDataSet(Integer stepId) {
        AmountSet dataSet = new AmountSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Amounts";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );

        Integer order = 1;

        for(AmountType amountType: accountType.getAmountTypes() ){
            AmountElement element = new AmountElement();
            element.isRequired = true;
            element.labelName = amountType.getLabelName();
            element.order = order;
            element.propertyName = amountType.getPropertyName();
            element.value = null;

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;
    }

    private RateSet createRateEntryDataSet(Integer stepId) {
        RateSet dataSet = new RateSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Rates";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );

        Integer order = 1;

        for(RateType rateType: accountType.getRateTypes() ){
            RateElement element = new RateElement();
            element.isRequired = true;
            element.labelName = rateType.getLabelName();
            element.order = order;
            element.propertyName = rateType.getPropertyName();
            element.value = null;

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;

    }

    private OptionSet createOptionEntryDataSet(Integer stepId) {
        OptionSet dataSet = new OptionSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Options";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );

        Integer order = 1;

        for(OptionType optionType: accountType.getOptionTypes() ){
            OptionElement element = new OptionElement();
            element.isRequired = true;
            element.labelName = optionType.getLabelName();
            element.order = order;
            element.propertyName = optionType.getPropertyName();
            element.value = this.account.getOptions().get(optionType.getPropertyName());

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;

    }

    private ScheduleSet createScheduleEntryDataSet(Integer stepId, Integer[] dependsOn) {
        ScheduleSet dataSet = new ScheduleSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Schedules";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );
        dataSet.dependsOnDataSets = dependsOn;

        Integer order = 1;

        for(ScheduleType scheduleType: accountType.getScheduleTypes() ){
            ScheduleElement element = new ScheduleElement();
            element.isRequired = true;
            element.labelName = scheduleType.getLabelName();
            element.order = order;
            element.propertyName = scheduleType.getPropertyName();
            element.value = new Schedule();

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;
    }

    private InstalmentSet createInstalmentEntryDataSet(Integer stepId, Integer[] dependsOn) {
        InstalmentSet dataSet = new InstalmentSet();

        dataSet.isValid = false;

        dataSet.dataSetId = stepId;
        dataSet.dataSetName = "Instalments";
        dataSet.url = String.format(this.stepUrlTemplate,this.processId, dataSet.dataSetName );
        dataSet.dependsOnDataSets = dependsOn;

        Integer order = 1;

        for(InstalmentType scheduleType: accountType.getInstalmentTypes() ){
            InstalmentElement element = new InstalmentElement();
            element.isRequired = true;
            element.labelName = scheduleType.getLabelName();
            element.order = order;
            element.propertyName = scheduleType.getPropertyName();
            element.value = new InstalmentSet();

            dataSet.data.add(element);

            order +=1;
        }

        return dataSet;
    }

}
