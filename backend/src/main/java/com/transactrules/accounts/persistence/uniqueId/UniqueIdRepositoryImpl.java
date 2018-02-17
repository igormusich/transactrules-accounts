package com.transactrules.accounts.persistence.uniqueId;

import com.transactrules.accounts.repository.UniqueIdRepository;
import com.transactrules.accounts.runtime.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueIdRepositoryImpl implements UniqueIdRepository {

    @Autowired
    JpaUniqueIdRepository dynamoRepository;

    @Override
    public void save(UniqueId uniqueId) {
        dynamoRepository.save(new UniqueIdDataObject(uniqueId));
    }

    @Override
    public UniqueId findOne(String className) {
        return dynamoRepository.findOne(className).getUniqueId();
    }
}
