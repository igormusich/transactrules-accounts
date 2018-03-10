package com.transactrules.accounts.runtime.repository;

import com.transactrules.accounts.runtime.domain.UniqueId;

public interface UniqueIdRepository  {
    void save(UniqueId uniqueId);
    UniqueId findOne(String className);
}
