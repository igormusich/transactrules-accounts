package com.transactrules.accounts.persistence;

import com.transactrules.accounts.DatabaseDriver;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Persistence;
import java.util.HashMap;

@Component
public class JpaDatabaseDriver implements DatabaseDriver {
    @Override
    public void generateDataModel() {
        //Persistence.generateSchema("accounts", new HashMap());
    }
}
