package com.transactrules.accounts.persistence.accountType;

import org.springframework.data.repository.CrudRepository;

public interface JpaAccountTypeRepository extends CrudRepository<AccountTypeDataObject, String> {

}
