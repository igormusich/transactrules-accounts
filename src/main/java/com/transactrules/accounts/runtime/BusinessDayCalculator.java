package com.transactrules.accounts.runtime;

import com.transactrules.accounts.configuration.BusinessDayCalculation;

import java.time.LocalDate;

public interface BusinessDayCalculator
{
    LocalDate GetCalculatedBusinessDay(LocalDate date, BusinessDayCalculation calculation);
}