package com.transactrules.accounts.persistence.transactionSet;

import org.springframework.data.repository.CrudRepository;


public interface JpaTransactionSetRepository extends CrudRepository<TransactionSetDataObject,String> {
}
