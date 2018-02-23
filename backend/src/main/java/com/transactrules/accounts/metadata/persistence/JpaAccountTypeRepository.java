package com.transactrules.accounts.metadata.persistence;

import org.springframework.data.repository.CrudRepository;

public interface JpaAccountTypeRepository extends CrudRepository<AccountTypeDataObject, String> {

}
