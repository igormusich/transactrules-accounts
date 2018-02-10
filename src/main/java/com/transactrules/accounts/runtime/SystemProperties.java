package com.transactrules.accounts.runtime;

import java.time.LocalDate;
import java.util.UUID;

public class SystemProperties {
    private String id;
    private LocalDate actionDate;

    public SystemProperties(){
        this.id = UUID.randomUUID().toString();
        this.actionDate = LocalDate.now();
    }

    public SystemProperties(String id, LocalDate actionDate) {
        this.id = id;
        this.actionDate = actionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }
}
