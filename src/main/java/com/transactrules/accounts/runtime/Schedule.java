package com.transactrules.accounts.runtime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.transactrules.accounts.configuration.BusinessDayCalculation;
import com.transactrules.accounts.configuration.ScheduleEndType;
import com.transactrules.accounts.configuration.ScheduleFrequency;
import com.transactrules.accounts.configuration.ScheduleType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@DynamoDBDocument
public class Schedule  {

    private String scheduleTypeId;

    private LocalDate startDate;

    private int endType;

    private int frequency;

    private LocalDate endDate;

    private int interval;

    private Integer numberOfRepeats;

    private int businessDayCalculation;

    private List<ScheduleDate> includeDates = new ArrayList<>();

    private List<ScheduleDate> excludeDates = new ArrayList<>();

    public Schedule() {

    }

    public Schedule(ScheduleType scheduleType){
        scheduleTypeId = scheduleType.getId();
    }

    @DynamoDBAttribute
    public String getScheduleTypeId() {
        return scheduleTypeId;
    }

    public void setScheduleTypeId(String scheduleTypeId) {
        this.scheduleTypeId = scheduleTypeId;
    }

    @DynamoDBAttribute
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute
    public int getEndType() {
        return endType;
    }

    public void setEndType(int endType) {
        this.endType = endType;
    }

    @DynamoDBAttribute
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @DynamoDBAttribute
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @DynamoDBAttribute
    public Integer getNumberOfRepeats() {
        return numberOfRepeats;
    }

    public void setNumberOfRepeats(Integer numberOfRepeats) {
        this.numberOfRepeats = numberOfRepeats;
    }

    @DynamoDBAttribute
    public int getBusinessDayCalculation() {
        return businessDayCalculation;
    }

    public void setBusinessDayCalculation(int businessDayCalculation) {
        this.businessDayCalculation = businessDayCalculation;
    }

    @DynamoDBAttribute
    public List<ScheduleDate> getIncludeDates() {
        return includeDates;
    }

    public void setIncludeDates(List<ScheduleDate> includeDates) {
        this.includeDates = includeDates;
    }

    @DynamoDBAttribute
    public List<ScheduleDate> getExcludeDates() {
        return excludeDates;
    }

    public void setExcludeDates(List<ScheduleDate> excludeDates) {
        this.excludeDates = excludeDates;
    }

    public BusinessDayCalculator getBusinessDayCalculator() {
        return businessDayCalculator;
    }

    public void setBusinessDayCalculator(BusinessDayCalculator businessDayCalculator) {
        this.businessDayCalculator = businessDayCalculator;
    }

    public Map<LocalDate, List<LocalDate>> getCachedDates() {
        return cachedDates;
    }

    public void setCachedDates(Map<LocalDate, List<LocalDate>> cachedDates) {
        this.cachedDates = cachedDates;
    }

    public transient BusinessDayCalculator businessDayCalculator;

    public Boolean IsDue(LocalDate date)
    {
        if (IsSimpleDailySchedule())
        {
            if (endType == ScheduleEndType.NoEnd.value())
            {
                return date.isBefore(startDate);
            }
            else if (endType == ScheduleEndType.EndDate.value())
            {
                return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                        ( date.isEqual(endDate) || date.isBefore(endDate));
            }
        }
        
        List<LocalDate> dates = GetAllDates(LastPossibleDate());

        return dates.contains(date);
    }

    //get next fifty years of dates assuming no schedule will be longer than that
    private LocalDate LastPossibleDate()
    {
        return startDate.plusYears(50);
    }

    private Boolean IsSimpleDailySchedule()
    {
        return this.frequency == ScheduleFrequency.Daily.value() && this.interval == 1 && this.businessDayCalculation == BusinessDayCalculation.AnyDay.value();
    }

    public Boolean IsDue()
    {
        return IsDue(SessionState.current().valueDate());
    }

    private transient Map<LocalDate, List<LocalDate>> cachedDates = new HashMap<>();

    public List<LocalDate> GetAllDates()
    {
        return GetAllDates(LastPossibleDate());
    }

    public List<LocalDate> GetAllDates(LocalDate toDate)
    {
        if (cachedDates.containsKey(toDate))
            return cachedDates.get(toDate);

        Set<LocalDate> dates = new HashSet<>();

        int repeats = 1;
        LocalDate iterator = startDate;
        LocalDate adjustedDate = businessDayCalculator.GetCalculatedBusinessDay(iterator, BusinessDayCalculation.fromInteger(this.businessDayCalculation));

        while (!IsCompleted(repeats, adjustedDate, toDate))
        {
            dates.add(adjustedDate);

            iterator = GetNextDate(repeats, iterator);

            adjustedDate = businessDayCalculator.GetCalculatedBusinessDay(iterator, BusinessDayCalculation.fromInteger(this.businessDayCalculation));

            repeats++;
        }

        for(ScheduleDate includeDate: this.includeDates){
            dates.add( includeDate.value);
        }


        for(ScheduleDate excludeDate : this.excludeDates)
        {
            dates.remove(excludeDate.value);
        }

        List<LocalDate> sortedDates = dates.stream().sorted().collect(Collectors.toList());
        cachedDates.put(toDate,sortedDates );

        return sortedDates;
    }

    private LocalDate GetNextDate(int repeats, LocalDate iterator)
    {
        if (this.frequency == ScheduleFrequency.Daily.value())
        {
            iterator = startDate.plusDays(interval * repeats);
        }
        else
        {
            iterator = startDate.plusMonths(interval * repeats);
        }

        return iterator;
    }

    private Boolean IsCompleted(Integer repeats, LocalDate lastDate, LocalDate endDate)
    {
        if (lastDate.isAfter(endDate))
        {
            return true;
        }

        if (this.endType == ScheduleEndType.EndDate.value() && lastDate.isAfter(endDate))
        {
            return true;
        }


        if (this.endType == ScheduleEndType.NoEnd.value())
        {
            return false;
        }

        if (this.endType == ScheduleEndType.Repeats.value() &&
                repeats > this.numberOfRepeats)
        {
            return true;
        }

        return lastDate.isAfter(this.endDate);

    }
}
