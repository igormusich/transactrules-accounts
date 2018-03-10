package com.transactrules.accounts.runtime.repository;

import com.transactrules.accounts.runtime.domain.SystemProperties;

public interface SystemPropertiesRepository  {
    void save(SystemProperties properties);
    SystemProperties findOne();

}
