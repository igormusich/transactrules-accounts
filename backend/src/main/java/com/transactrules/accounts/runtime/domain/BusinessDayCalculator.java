package com.transactrules.accounts.runtime.domain;

import com.transactrules.accounts.metadata.domain.BusinessDayCalculation;

import java.time.LocalDate;

public interface BusinessDayCalculator
{
    LocalDate GetCalculatedBusinessDay(LocalDate date, BusinessDayCalculation calculation);
}