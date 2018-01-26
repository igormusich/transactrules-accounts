package com.transactrules.accounts.repository;

import com.transactrules.accounts.runtime.SystemProperties;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface SystemPropertiesRepository extends CrudRepository<SystemProperties,String> {

}
