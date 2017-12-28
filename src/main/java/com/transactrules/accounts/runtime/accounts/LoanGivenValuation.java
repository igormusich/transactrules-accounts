/*package com.transactrules.accounts.runtime.accounts;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.runtime.DateValue;
import com.transactrules.accounts.runtime.AmountValue;
import com.transactrules.accounts.runtime.OptionValue;
import com.transactrules.accounts.runtime.Position;
import com.transactrules.accounts.runtime.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanGivenValuation extends AccountValuation {

    private Position _InterestCapitalized;
    private Position _EarlyRedemptionFee;
    private Position _InterestAccrued;
    private Position _Principal;
    private Position _ConversionInterest;

    private DateValue _AccrualStart;
    private DateValue _StartDate;
    private DateValue _EndDate;

    private AmountValue _ConversionInterestAmount;
    private AmountValue _AdditionalAdvanceAmount;
    private AmountValue _AdvanceAmount;
    private AmountValue _RedemptionAmount;

    private OptionValue _AccrualOption;

    public BigDecimal InterestCapitalized() {
        return _InterestCapitalized.amount();
    }
    public BigDecimal EarlyRedemptionFee() {
        return _EarlyRedemptionFee.amount();
    }
    public BigDecimal InterestAccrued() {
        return _InterestAccrued.amount();
    }
    public BigDecimal Principal() {
        return _Principal.amount();
    }
    public BigDecimal ConversionInterest() {
        return _ConversionInterest.amount();
    }

    public LocalDate AccrualStart(){
        return _AccrualStart.date();
    }
    public LocalDate StartDate(){
        return _StartDate.date();
    }
    public LocalDate EndDate(){
        return _EndDate.date();
    }

    public BigDecimal ConversionInterestAmount(){
        return _ConversionInterestAmount.amount();
    }
    public BigDecimal AdditionalAdvanceAmount(){
        return _AdditionalAdvanceAmount.amount();
    }
    public BigDecimal AdvanceAmount(){
        return _AdvanceAmount.amount();
    }
    public BigDecimal RedemptionAmount(){
        return _RedemptionAmount.amount();
    }

    public String AccrualOption(){
        return _AccrualOption.value();
    }

    @Override
    public void initialize(Account account, AccountType accountType) {

        super.initialize(account, accountType);

        //initialize dates

        _AccrualStart= account.dates().get(accountType.getDateTypeByName("AccrualStart").get().id());
        _StartDate= account.dates().get(accountType.getDateTypeByName("StartDate").get().id());
        _EndDate= account.dates().get(accountType.getDateTypeByName("EndDate").get().id());

        //initialize positions

        _InterestCapitalized= account.positions().get(accountType.getPositionTypeByName("InterestCapitalized").get().id());
        _EarlyRedemptionFee= account.positions().get(accountType.getPositionTypeByName("EarlyRedemptionFee").get().id());
        _InterestAccrued= account.positions().get(accountType.getPositionTypeByName("InterestAccrued").get().id());
        _Principal= account.positions().get(accountType.getPositionTypeByName("Principal").get().id());
        _ConversionInterest= account.positions().get(accountType.getPositionTypeByName("ConversionInterest").get().id());

        //initialize amounts

        _ConversionInterestAmount= account.amounts().get(accountType.getAmountTypeByName("ConversionInterestAmount").get().id());
        _AdditionalAdvanceAmount= account.amounts().get(accountType.getAmountTypeByName("AdditionalAdvanceAmount").get().id());
        _AdvanceAmount= account.amounts().get(accountType.getAmountTypeByName("AdvanceAmount").get().id());
        _RedemptionAmount= account.amounts().get(accountType.getAmountTypeByName("RedemptionAmount").get().id());

        //initialize options

        _AccrualOption= account.options().get(accountType.getOptionTypeByName("AccrualOption").get().id());
        _AccrualOption.setValues(com.transactrules.accounts.calculations.AccrualCalculation.AccrualOptions());

    }

    @Override
    public void processTransaction(Transaction transaction) {

    }

    @Override
    public void startOfDay() {

    }

    @Override
    public void endOfOfDay() {

    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void calculateInstaments() {

    }
}
*/
