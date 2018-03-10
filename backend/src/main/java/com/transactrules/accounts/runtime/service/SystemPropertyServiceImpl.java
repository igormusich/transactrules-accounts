package com.transactrules.accounts.runtime.service;

import com.transactrules.accounts.runtime.repository.SystemPropertiesRepository;
import com.transactrules.accounts.runtime.domain.SystemProperties;
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
            properties = systemPropertiesRepository.findOne();
        }

        return properties;
    }
}
