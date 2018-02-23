package com.transactrules.accounts.runtime.persistence.uniqueId;


import org.springframework.data.repository.CrudRepository;

public interface JpaUniqueIdRepository extends CrudRepository<UniqueIdDataObject,String> {

}
