package com.transactrules.accounts.services;

import com.transactrules.accounts.repository.SystemPropertiesRepository;
import com.transactrules.accounts.runtime.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SystemPropertyServiceImpl implements SystemPropertyService {

    @Autowired
    SystemPropertiesRepository systemPropertiesRepository;

    static SystemProperties properties;


    @Override
    public LocalDate getActionDate() {

        return getProperties().getActionDate();
    }

    @Override
    public void incrementActionDate() {
        getProperties().setActionDate(getProperties().getActionDate().plusDays(1));

        systemPropertiesRepository.save(getProperties());
    }

    synchronized private SystemProperties getProperties() {

        if (properties == null) {
            properties = systemPropertiesRepository.findAll().iterator().next();
        }

        return properties;
    }
}
