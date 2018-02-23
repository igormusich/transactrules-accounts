package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.domain.SystemProperties;

public interface SystemPropertiesRepository  {
    void save(SystemProperties properties);
    SystemProperties findOne();

}
