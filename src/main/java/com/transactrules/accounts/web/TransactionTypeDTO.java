package com.transactrules.accounts.web;

import java.util.ArrayList;
import java.util.List;

public class TransactionTypeDTO {
    public String name;
    public boolean maximumPrecision;
    public List<String> creditPositionNames = new ArrayList<>();
    public List<String> debitPositionNames = new ArrayList<>();
}
