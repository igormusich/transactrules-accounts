package com.transactrules.accounts.runtime.persistence.systemProperties;

import com.transactrules.accounts.runtime.repository.SystemPropertiesRepository;
import com.transactrules.accounts.runtime.domain.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemPropertiesRepositoryImpl implements SystemPropertiesRepository {
    @Autowired
    JpaSystemPropertiesRepository dynamoRepository;

    @Override
    public void save(SystemProperties properties) {
        dynamoRepository.save(new SystemPropertiesDataObject(properties));
    }

    @Override
    public SystemProperties findOne() {

        return dynamoRepository.findAll().iterator().next().getProperties();
    }
}
