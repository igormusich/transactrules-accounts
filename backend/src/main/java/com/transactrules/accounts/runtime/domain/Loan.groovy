package com.transactrules.accounts.runtime.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.transactrules.accounts.metadata.domain.ScheduledTransactionTiming
import com.transactrules.accounts.utilities.Solver

import java.math.RoundingMode
import java.time.LocalDate

class Loan extends Account {


    @JsonIgnore()
    Position _ConversionInterest
    @JsonIgnore()
    Position _EarlyRedemptionFee
    @JsonIgnore()
    Position _InterestAccrued
    @JsonIgnore()
    Position _InterestCapitalized
    @JsonIgnore()
    Position _Principal


    @JsonIgnore()
    DateValue _StartDate
    @JsonIgnore()
    DateValue _AccrualStart
    @JsonIgnore()
    DateValue _EndDate


    @JsonIgnore()
    AmountValue _AdvanceAmount


    @JsonIgnore()
    OptionValue _AccrualOption


    @JsonIgnore()
    RateValue _InterestRate


    @JsonIgnore()
    Schedule AccrualSchedule
    @JsonIgnore()
    Schedule InterestSchedule
    @JsonIgnore()
    Schedule RedemptionSchedule


    @JsonIgnore()
    InstalmentSet Redemptions


    @JsonIgnore()
    BigDecimal ConversionInterest() {
        return _ConversionInterest.amount
    }
    @JsonIgnore()
    BigDecimal EarlyRedemptionFee() {
        return _EarlyRedemptionFee.amount
    }
    @JsonIgnore()
    BigDecimal InterestAccrued() {
        return _InterestAccrued.amount
    }
    @JsonIgnore()
    BigDecimal InterestCapitalized() {
        return _InterestCapitalized.amount
    }
    @JsonIgnore()
    BigDecimal Principal() {
        return _Principal.amount
    }


    @JsonIgnore()
    LocalDate StartDate(){
        return _StartDate.data
    }
    @JsonIgnore()
    LocalDate AccrualStart(){
        return _AccrualStart.data
    }
    @JsonIgnore()
    LocalDate EndDate(){
        return _EndDate.data
    }


    @JsonIgnore()
    BigDecimal AdvanceAmount(){
        return _AdvanceAmount.amount
    }


    @JsonIgnore()
    String AccrualOption(){
        return _AccrualOption.value
    }


    @JsonIgnore()
    BigDecimal InterestRate(){
        return _InterestRate.value
    }


    Loan(Account prototype){
        super(prototype)
    }

    Loan() {

        //initialize options

        _AccrualOption = new OptionValue()
        options.put('AccrualOption', _AccrualOption)
        _AccrualOption.values=com.transactrules.accounts.calculations.AccrualCalculation.AccrualOptions()

    }

    @Override
    void setCalculated() {

        super.setCalculated()

        //initialize dates

        _StartDate= dates.get('StartDate')
        _AccrualStart= dates.get('AccrualStart')
        _EndDate= dates.get('EndDate')

        //initialize positions

        if(!positions.containsKey ('ConversionInterest')){
            positions.put('ConversionInterest', new Position())
        }
        _ConversionInterest = positions.get('ConversionInterest');
        if(!positions.containsKey ('EarlyRedemptionFee')){
            positions.put('EarlyRedemptionFee', new Position())
        }
        _EarlyRedemptionFee = positions.get('EarlyRedemptionFee');
        if(!positions.containsKey ('InterestAccrued')){
            positions.put('InterestAccrued', new Position())
        }
        _InterestAccrued = positions.get('InterestAccrued');
        if(!positions.containsKey ('InterestCapitalized')){
            positions.put('InterestCapitalized', new Position())
        }
        _InterestCapitalized = positions.get('InterestCapitalized');
        if(!positions.containsKey ('Principal')){
            positions.put('Principal', new Position())
        }
        _Principal = positions.get('Principal');

        //initialize amounts

        _AdvanceAmount= amounts.get('AdvanceAmount')

        //initialize rates
        _InterestRate= rates.get('InterestRate')

        _AccrualOption = options.get('AccrualOption')

        //initialize schedules

        if(!schedules.containsKey('AccrualSchedule'))
        {
            Schedule schedule = new Schedule()
            schedules.put('AccrualSchedule', schedule)

        }
        AccrualSchedule = schedules.get('AccrualSchedule')
        AccrualSchedule.setBusinessDayCalculator businessDayCalculator
        SetAccrualScheduleCalculatedProperties(AccrualSchedule)

        if(!schedules.containsKey('InterestSchedule'))
        {
            Schedule schedule = new Schedule()
            schedules.put('InterestSchedule', schedule)

            //set default properties only when creating new schedule
            SetInterestScheduleDefaultProperties(schedule)
        }
        InterestSchedule = schedules.get('InterestSchedule')
        InterestSchedule.setBusinessDayCalculator businessDayCalculator

        if(!schedules.containsKey('RedemptionSchedule'))
        {
            Schedule schedule = new Schedule()
            schedules.put('RedemptionSchedule', schedule)

            //set default properties only when creating new schedule
            SetRedemptionScheduleDefaultProperties(schedule)
        }
        RedemptionSchedule = schedules.get('RedemptionSchedule')
        RedemptionSchedule.setBusinessDayCalculator businessDayCalculator


        if(!this.instalmentSets.containsKey('Redemptions'))
        {
            Redemptions = new InstalmentSet()
            instalmentSets.put('Redemptions', Redemptions)
        }
        else {
            Redemptions = instalmentSets.get('Redemptions')
        }
    }

    void InitializeRedemptions(){
        RedemptionSchedule.GetAllDates().each {
            LocalDate data ->
                Redemptions.instalments.put(data, new InstalmentValue( BigDecimal.ZERO, false))
        }
    }



    void SetAccrualScheduleCalculatedProperties(Schedule schedule)
    {
        schedule.startDate = this.StartDate()
        schedule.interval = 1
        schedule.businessDayCalculation = 'ANY_DAY'
        schedule.frequency ='DAILY'
        schedule.endType = 'NO_END'
    }


    void SetInterestScheduleDefaultProperties(Schedule schedule)
    {
        schedule.businessDayCalculation ='ANY_DAY'
        schedule.frequency ='MONTHLY'
        schedule.endType = 'END_DATE'
        schedule.startDate = StartDate()
        schedule.endDate = EndDate()
        schedule.interval = 1
        schedule.includeDates = fromDates(EndDate())

    }

    void SetRedemptionScheduleDefaultProperties(Schedule schedule)
    {
        schedule.businessDayCalculation ='ANY_DAY'
        schedule.frequency ='MONTHLY'
        schedule.endType = 'END_DATE'
        schedule.startDate = StartDate().plusMonths(1)
        schedule.endDate = EndDate()
        schedule.interval = 1
        schedule.includeDates = fromDates(EndDate())

    }

    @Override
    Map<String, BigDecimal> processTransaction(String transactionTypeName, BigDecimal amount){
        Map<String,BigDecimal> positionMap = new HashMap<>()

        switch(transactionTypeName) {
            case 'InterestAccrued':
                _InterestAccrued.add(amount)

                positionMap.put('InterestAccrued', _InterestAccrued.amount)
                break
            case 'InterestCapitalized':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _Principal.add(amount)

                positionMap.put('Principal', _Principal.amount)
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _InterestAccrued.subtract(amount)

                positionMap.put('InterestAccrued', _InterestAccrued.amount)
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _InterestCapitalized.add(amount)

                positionMap.put('InterestCapitalized', _InterestCapitalized.amount)
                break
            case 'Redemption':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _Principal.subtract(amount)

                positionMap.put('Principal', _Principal.amount)
                break
            case 'Advance':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _Principal.add(amount)

                positionMap.put('Principal', _Principal.amount)
                break
            case 'AdditionalAdvance':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _Principal.add(amount)

                positionMap.put('Principal', _Principal.amount)
                break
            case 'ConversionInterest':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _ConversionInterest.add(amount)

                positionMap.put('ConversionInterest', _ConversionInterest.amount)
                break
            case 'EarlyRedemptionFee':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _EarlyRedemptionFee.add(amount)

                positionMap.put('EarlyRedemptionFee', _EarlyRedemptionFee.amount)
                break
            case 'FXResultInterest':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _InterestAccrued.add(amount)

                positionMap.put('InterestAccrued', _InterestAccrued.amount)
                break
            case 'FXResultPrincipal':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _Principal.add(amount)

                positionMap.put('Principal', _Principal.amount)
                break
            case 'InterestPayment':
                amount = amount.setScale(2, RoundingMode.HALF_DOWN)
                _InterestAccrued.subtract(amount)

                positionMap.put('InterestAccrued', _InterestAccrued.amount)
                break
            default:
                throw new IllegalArgumentException('Invalid transactionTypeName : ' + transactionTypeName)
        }

        return positionMap
    }

    @Override
    void startOfDay() {

        if (_StartDate.isDue(valueDate)){
            BigDecimal amount = AdvanceAmount()
            createTransaction('Advance', amount)
        }
        if(Redemptions.instalments.containsKey(valueDate)){
            Map<LocalDate,InstalmentValue> instalments = Redemptions.instalments
            InstalmentValue instalmentValue=instalments.get(valueDate)
            createTransaction('Redemption',instalmentValue.amount)
        }
    }

    @Override
    void endOfOfDay() {
        if (AccrualSchedule.isDue(valueDate)){
            BigDecimal amount = com.transactrules.accounts.calculations.AccrualCalculation.InterestAccrued(AccrualOption(), Principal(), InterestRate(), ValueDate())
            createTransaction('InterestAccrued', amount)
        }
        if (InterestSchedule.isDue(valueDate)){
            BigDecimal amount = InterestAccrued()
            createTransaction('InterestCapitalized', amount)
        }
    }

    @Override
    void onDataChanged() {

    }

    @Override
    void calculateInstaments() {
        if (Redemptions.instalments.size()==0){
            InitializeRedemptions()
        }
        CalculateRedemptionsInstalments()
    }

    BigDecimal GetClosingBalanceForRedemptions(BigDecimal value) {

        this.setFutureInstalmentValue('Redemptions', ScheduledTransactionTiming.StartOfDay, value)

        List<LocalDate> dates = RedemptionSchedule.GetAllDates()

        LocalDate lastDate = dates.get(dates.size()-1)

        this.snapshot()

        this.forecast(lastDate)

        BigDecimal result = Principal()

        this.restoreSnapshot()

        return result
    }

    class RedemptionsCalculation implements Calculation {
        Loan account

        RedemptionsCalculation(Loan param){
            account = param
        }

        BigDecimal calculate(BigDecimal amount) {
            return account.GetClosingBalanceForRedemptions(amount)
        }
    }

    void CalculateRedemptionsInstalments() {
        Solver solver = new Solver()
        BigDecimal amount = solver.FindFunctionZero( new RedemptionsCalculation(this),  BigDecimal.ZERO, BigDecimal.valueOf(1000000000000000L), BigDecimal.valueOf(0.01) );

        amount = amount.setScale(2, RoundingMode.HALF_UP)

        this.setFutureInstalmentValue('Redemptions', ScheduledTransactionTiming.StartOfDay, amount)
    }
    @Override
    String generatedAt(){
        return '2018-02-16T23:18:01.008'
    }


    @Override
    LocalDate retrieveStartDate(){
        LocalDate startDate = null

        startDate = StartDate()

        return startDate;
    }
}