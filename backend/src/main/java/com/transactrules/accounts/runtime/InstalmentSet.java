package com.transactrules.accounts.runtime;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class InstalmentSet  {

    public InstalmentSet(){

    }

    public InstalmentSet(InstalmentSet prototype){
        this.instalments = new HashMap<>(prototype.instalments);
    }

    private Map<LocalDate, InstalmentValue> instalments = new HashMap<>();

    public Map<LocalDate, InstalmentValue> getInstalments() {
        return instalments;
    }

    public void setInstalments(Map<LocalDate, InstalmentValue> instalments) {
        this.instalments = instalments;
    }

}
