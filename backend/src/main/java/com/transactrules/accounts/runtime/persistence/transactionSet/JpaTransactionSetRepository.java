package com.transactrules.accounts.runtime.persistence.transactionSet;

import org.springframework.data.repository.CrudRepository;


public interface JpaTransactionSetRepository extends CrudRepository<TransactionSetDataObject,String> {
}
