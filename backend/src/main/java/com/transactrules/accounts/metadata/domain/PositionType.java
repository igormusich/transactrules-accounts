package com.transactrules.accounts.metadata.domain;

import com.transactrules.accounts.NamedAbstractEntity;


/**
 * Define metadata for position type
 */

public class PositionType  extends NamedAbstractEntity {

    private Boolean isPrincipal=false;

    public PositionType() {

    }

    public PositionType( String name) {
        super(name);
    }

    public Boolean getPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        isPrincipal = principal;
    }
}
