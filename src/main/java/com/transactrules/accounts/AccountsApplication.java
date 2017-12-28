package com.transactrules.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*@EntityScan(
		basePackageClasses = { AccountsApplication.class, Jsr310JpaConverters.class }
)*/
@SpringBootApplication
public class AccountsApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccountsApplication.class, args);

	}

}
