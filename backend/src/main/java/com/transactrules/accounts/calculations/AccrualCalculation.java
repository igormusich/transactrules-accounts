package com.transactrules.accounts.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccrualCalculation {
    public static List<String> AccrualOptions() {
        return new ArrayList<>(Arrays.asList("Actual", "360", "365", "30/360"));
    }

    public static BigDecimal InterestAccrued(
            String accrualOption,
            BigDecimal principal,
            BigDecimal rate,
            LocalDate valueDate)
    {
        BigDecimal accrued;
        BigDecimal divisor= new BigDecimal(365) ;

        switch (accrualOption)
        {
            case "Actual":
                if ( valueDate.isLeapYear())
                {
                    divisor =  new BigDecimal(366) ;
                }
                break;
            case "360":
                divisor = new BigDecimal(  360);
                break;
            case "30/360":
                divisor = new BigDecimal(  360);
                break;
        }

        accrued = principal.multiply(rate).divide(divisor, 16, RoundingMode.HALF_DOWN);

        if (accrualOption.equalsIgnoreCase("30/360")  && valueDate.isEqual( valueDate.with(TemporalAdjusters.lastDayOfMonth())))
        {
            switch (valueDate.getDayOfMonth())
            {
                case 28:
                    accrued = accrued.multiply(new BigDecimal(3));
                    break;
                case 29:
                    accrued = accrued.multiply(new BigDecimal(2));
                    break;
                case 31:
                    accrued = new BigDecimal(0);
                    break;
            }
        }

        return accrued;

        //var a = InterestAccrued(rate: 1, valueDate: DateTime.Now, accrualOption: "360", principal: 100);
    }
}

