package com.transactrules.accounts.runtime.repository;

import com.transactrules.accounts.runtime.domain.Account;

import java.util.List;

public interface AccountRepository  {
    void save(Account account);
    List<Account> findAll();
    Account findOne(String key);

}
