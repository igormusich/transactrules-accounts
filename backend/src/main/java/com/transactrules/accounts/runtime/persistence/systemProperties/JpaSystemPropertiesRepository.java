package com.transactrules.accounts.runtime.persistence.systemProperties;

import org.springframework.data.repository.CrudRepository;


public interface JpaSystemPropertiesRepository extends CrudRepository<SystemPropertiesDataObject,String> {

}
