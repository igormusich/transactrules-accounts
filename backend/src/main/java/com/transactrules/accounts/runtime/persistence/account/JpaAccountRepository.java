package com.transactrules.accounts.runtime.persistence.account;


import org.springframework.data.repository.CrudRepository;


public interface JpaAccountRepository extends CrudRepository<AccountDataObject,String> {

}
