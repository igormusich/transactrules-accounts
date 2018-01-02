package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.services.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    @Autowired
    private AccountValidator accountValidator;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create new Account")
    public ResponseEntity<?> create(@Valid @RequestBody Account account ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (account == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Account savedItem = service.create(account.getAccountTypeName(),account.getAccountNumber());

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedItem.getAccountNumber()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all Accounts", response = AccountType.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<Account> accounts = service.findAll();

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation(value = "Get Account by Account Number", response = AccountType.class, responseContainer="List")
    public ResponseEntity<?> findByAccountNumber( @PathVariable("id") String accountNumber){
        Account account = service.findByAccountNumber(accountNumber);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @InitBinder("account")
    public void setupBinder(WebDataBinder binder) {

        binder.addValidators(accountValidator);
    }
}
