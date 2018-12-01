package com.transactrules.accounts.runtime.persistence.uniqueId;

import com.transactrules.accounts.runtime.repository.UniqueIdRepository;
import com.transactrules.accounts.runtime.domain.UniqueId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        Optional<UniqueIdDataObject> result= dynamoRepository.findById(className);

        if(result.isPresent()){
            return result.get().getUniqueId();
        }

        return null;
    }
}
