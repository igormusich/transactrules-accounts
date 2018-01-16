package com.transactrules.accounts.runtime;

import com.transactrules.accounts.metadata.ScheduledTransactionTiming;
import com.transactrules.accounts.utilities.Solver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class localLoanGiven extends Account {

    private Position _ConversionInterest;
    private Position _EarlyRedemptionFee;
    private Position _InterestAccrued;
    private Position _InterestCapitalized;
    private Position _Principal;

    private DateValue _StartDate;
    private DateValue _AccrualStart;
    private DateValue _EndDate;

    private AmountValue _RedemptionAmount;
    private AmountValue _AdditionalAdvanceAmount;
    private AmountValue _ConversionInterestAmount;
    private AmountValue _AdvanceAmount;

    private OptionValue _AccrualOption;

    private RateValue _InterestRate;

    private Schedule _AccrualSchedule;
    private Schedule _InterestSchedule;
    private Schedule _RedemptionSchedule;

    private InstalmentSet _Redemptions;

    public BigDecimal ConversionInterest() {
        return _ConversionInterest.getAmount();
    }
    public BigDecimal EarlyRedemptionFee() {
        return _EarlyRedemptionFee.getAmount();
    }
    public BigDecimal InterestAccrued() {
        return _InterestAccrued.getAmount();
    }
    public BigDecimal InterestCapitalized() {
        return _InterestCapitalized.getAmount();
    }
    public BigDecimal Principal() {
        return _Principal.getAmount();
    }

    public LocalDate StartDate(){
        return _StartDate.getDate();
    }
    public LocalDate AccrualStart(){
        return _AccrualStart.getDate();
    }
    public LocalDate EndDate(){
        return _EndDate.getDate();
    }

    public BigDecimal RedemptionAmount(){
        return _RedemptionAmount.getAmount();
    }
    public BigDecimal AdditionalAdvanceAmount(){
        return _AdditionalAdvanceAmount.getAmount();
    }
    public BigDecimal ConversionInterestAmount(){
        return _ConversionInterestAmount.getAmount();
    }
    public BigDecimal AdvanceAmount(){
        return _AdvanceAmount.getAmount();
    }

    public String AccrualOption(){
        return _AccrualOption.getValue();
    }

    public Schedule AccrualSchedule(){
        return _AccrualSchedule;
    }
    public Schedule InterestSchedule(){
        return _InterestSchedule;
    }
    public Schedule RedemptionSchedule(){
        return _RedemptionSchedule;
    }

    public InstalmentSet Redemptions(){
        return _Redemptions;
    }

    public BigDecimal InterestRate(){
        return _InterestRate.getValue();
    }

    public localLoanGiven() {

        //initialize options

        _AccrualOption = new OptionValue();
        this.getOptions().put("AccrualOption", _AccrualOption );
        _AccrualOption = this.getOptions().get("AccrualOption");
        _AccrualOption.setValues(com.transactrules.accounts.calculations.AccrualCalculation.AccrualOptions());

    }

    @Override
    public void setCalculated() {

        super.setCalculated();

        //initialize dates

        _StartDate= this.getDates().get("StartDate");
        _AccrualStart= this.getDates().get("AccrualStart");
        _EndDate= this.getDates().get("EndDate");

        //initialize positions

        _ConversionInterest = new Position();
        this.getPositions().put("ConversionInterest", _ConversionInterest);
        _EarlyRedemptionFee = new Position();
        this.getPositions().put("EarlyRedemptionFee", _EarlyRedemptionFee);
        _InterestAccrued = new Position();
        this.getPositions().put("InterestAccrued", _InterestAccrued);
        _InterestCapitalized = new Position();
        this.getPositions().put("InterestCapitalized", _InterestCapitalized);
        _Principal = new Position();
        this.getPositions().put("Principal", _Principal);

        //initialize amounts

        _RedemptionAmount= this.getAmounts().get("RedemptionAmount");
        _AdditionalAdvanceAmount= this.getAmounts().get("AdditionalAdvanceAmount");
        _ConversionInterestAmount= this.getAmounts().get("ConversionInterestAmount");
        _AdvanceAmount= this.getAmounts().get("AdvanceAmount");

        //initialize rates
        _InterestRate= this.getRates().get("InterestRate");

        //initialize schedules

        if(!this.getSchedules().containsKey("AccrualSchedule"))
        {
            Schedule schedule = new Schedule();
            this.getSchedules().put("AccrualSchedule", schedule);
        }
        _AccrualSchedule = this.getSchedules().get("AccrualSchedule");
        _AccrualSchedule.setBusinessDayCalculator(this.businessDayCalculator);
        SetAccrualScheduleCalculatedProperties(_AccrualSchedule);

        if(!this.getSchedules().containsKey("InterestSchedule"))
        {
            Schedule schedule = new Schedule();
            this.getSchedules().put("InterestSchedule", schedule);
        }
        _InterestSchedule = this.getSchedules().get("InterestSchedule");
        _InterestSchedule.setBusinessDayCalculator(this.businessDayCalculator);
        SetInterestScheduleDefaultProperties(_InterestSchedule);

        if(!this.getSchedules().containsKey("RedemptionSchedule"))
        {
            Schedule schedule = new Schedule();
            this.getSchedules().put("RedemptionSchedule", schedule);
        }
        _RedemptionSchedule = this.getSchedules().get("RedemptionSchedule");
        _RedemptionSchedule.setBusinessDayCalculator(this.businessDayCalculator);
        SetRedemptionScheduleDefaultProperties(_RedemptionSchedule);


        if(!this.getInstalmentSets().containsKey("Redemptions"))
        {
            _Redemptions = new InstalmentSet();
            this.getInstalmentSets().put("Redemptions", _Redemptions);
        }
        else {
            _Redemptions = this.getInstalmentSets().get("Redemptions");
        }
    }

    public void InitializeRedemptions(){
        for(LocalDate date : _RedemptionSchedule.GetAllDates()){
            _Redemptions.getInstalments().put(date, new InstalmentValue( BigDecimal.ZERO, false));
        }
    }

    public void SetAccrualScheduleCalculatedProperties(Schedule schedule)
    {
        schedule.setStartDate(this.StartDate());
        schedule.setInterval(1);
        schedule.setBusinessDayCalculation("ANY_DAY");
        schedule.setFrequency("DAILY");
        schedule.setEndType("NO_END");
    }

    public void SetInterestScheduleDefaultProperties(Schedule schedule)
    {
        schedule.setBusinessDayCalculation("ANY_DAY");
        schedule.setFrequency("MONTHLY");
        schedule.setEndType("END_DATE");
        schedule.setInterval(1);
    }

    public void SetRedemptionScheduleDefaultProperties(Schedule schedule)
    {
        schedule.setBusinessDayCalculation("ANY_DAY");
        schedule.setFrequency("MONTHLY");
        schedule.setEndType("END_DATE");
        schedule.setInterval(1);
    }

    @Override
    public void processTransaction(String transactionTypeName, BigDecimal amount){

        switch(transactionTypeName) {
            case "InterestAccrued":
                _InterestAccrued.add(amount);
                break;
            case "InterestCapitalized":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _Principal.add(amount);
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _InterestAccrued.subtract(amount);
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _InterestCapitalized.add(amount);
                break;
            case "Redemption":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _Principal.subtract(amount);
                break;
            case "Advance":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _Principal.add(amount);
                break;
            case "AdditionalAdvance":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _Principal.add(amount);
                break;
            case "ConversionInterest":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _ConversionInterest.add(amount);
                break;
            case "EarlyRedemptionFee":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _EarlyRedemptionFee.add(amount);
                break;
            case "FXResultInterest":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _InterestAccrued.add(amount);
                break;
            case "FXResultPrincipal":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _Principal.add(amount);
                break;
            case "InterestPayment":
                amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                _InterestAccrued.subtract(amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid transactionTypeName : " + transactionTypeName);
        }
    }

    @Override
    public void startOfDay() {

        if (_StartDate.isDue(valueDate)){
            BigDecimal amount = AdvanceAmount();
            createTransaction("Advance", amount);
        }
        if(Redemptions().getInstalments().containsKey(valueDate)){
            InstalmentValue instalmentValue=Redemptions().getInstalments().get(valueDate);
            createTransaction("Redemption",instalmentValue.getAmount());
        }
    }



    @Override
    public void endOfOfDay() {
        if (_AccrualSchedule.isDue(valueDate)){
            BigDecimal amount = com.transactrules.accounts.calculations.AccrualCalculation.InterestAccrued(AccrualOption(), Principal(), InterestRate(), ValueDate());
            createTransaction("InterestAccrued", amount);
        }
        if (_InterestSchedule.isDue(valueDate)){
            BigDecimal amount = InterestAccrued();
            createTransaction("InterestCapitalized", amount);
        }
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void calculateInstaments() {
        if (_Redemptions.getInstalments().size()==0){
            InitializeRedemptions();
        }
        CalculateRedemptionsInstalments();
    }

    public BigDecimal GetClosingBalanceForRedemptions(BigDecimal value) {

        this.setFutureInstalmentValue("Redemptions", ScheduledTransactionTiming.StartOfDay, value);

        List<LocalDate> dates = _RedemptionSchedule.GetAllDates();

        LocalDate lastDate = dates.get(dates.size()-1);

        this.snapshot();

        this.forecast(lastDate);

        BigDecimal result = Principal();

        this.restoreSnapshot();

        return result;
    }

    public void CalculateRedemptionsInstalments() {
        Solver solver = new Solver();
        BigDecimal amount = solver.FindFunctionZero( this::GetClosingBalanceForRedemptions,  BigDecimal.ZERO, BigDecimal.valueOf(1000000000000000L), BigDecimal.valueOf(0.01) );
    }
    @Override
    public String generatedAt(){
        return "2018-01-16T04:07:09.038";
    }
}