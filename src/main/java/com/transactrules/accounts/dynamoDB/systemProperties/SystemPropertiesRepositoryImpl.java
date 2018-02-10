package com.transactrules.accounts.dynamoDB.systemProperties;

import com.transactrules.accounts.repository.SystemPropertiesRepository;
import com.transactrules.accounts.runtime.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemPropertiesRepositoryImpl implements SystemPropertiesRepository {
    @Autowired
    DynamoSystemPropertiesRepository dynamoRepository;

    @Override
    public void save(SystemProperties properties) {
        dynamoRepository.save(new SystemPropertiesDataObject(properties));
    }

    @Override
    public SystemProperties findOne() {

        return dynamoRepository.findAll().iterator().next().getData();
    }
}
