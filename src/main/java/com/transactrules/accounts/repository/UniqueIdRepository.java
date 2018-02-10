package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.UniqueId;

public interface UniqueIdRepository  {
    void save(UniqueId uniqueId);
    UniqueId findOne(String className);
}
