package com.transactrules.accounts.runtime;


import java.time.LocalDate;
import java.util.Optional;

public class SessionState {

    private Optional<LocalDate> valueDate;
    private Optional<LocalDate> actionDate;


    public LocalDate valueDate() {

        if (!this.valueDate.isPresent()) {
            valueDate = Optional.of(actionDate());
        }

        return valueDate.get();
    }

    public void setValueDate(LocalDate value){
        valueDate = Optional.of(value);
    }


    public LocalDate actionDate() {

        if (!actionDate.isPresent()) {
            actionDate = Optional.of(LocalDate.now());
        }

        return actionDate.get();
    }

    public void setActionDate(LocalDate value){
        actionDate = Optional.of(value);
    }

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<SessionState> sessionState =
            new ThreadLocal<SessionState>() {
                @Override
                protected SessionState initialValue() {
                    return new SessionState();
                }
            };


    public static SessionState current() {

        return sessionState.get();
    }

}