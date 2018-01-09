package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.metadata.AccountTypeValidator;
import com.transactrules.accounts.services.AccountTypeService;
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
@RequestMapping(path="/accountTypes")
public class AccountTypeController {

    private final AccountTypeService service;
    private final AccountTypeValidator accountTypeValidator;

    @Autowired
    public AccountTypeController(AccountTypeService service,
                                 AccountTypeValidator accountTypeValidator){

        this.service = service;
        this.accountTypeValidator = accountTypeValidator;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Add a new AccountType", response = AccountType.class )
    public ResponseEntity<?> create(@Valid @RequestBody AccountType item )  {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (item == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        final AccountType savedItem = service.create(item);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{propertyName}")
                .buildAndExpand(savedItem.getClassName()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all AccountTypes", response = AccountType.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<AccountType> accountTypes = service.findAll();

        return new ResponseEntity<>(accountTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}", method= RequestMethod.GET)
    @ApiOperation(value = "Get AccountType by class Name", response = AccountType.class)
    public ResponseEntity<?> findByAccountNumber( @PathVariable("className") String className){
        AccountType accountType = service.findByClassName(className);

        return new ResponseEntity<>(accountType, HttpStatus.OK);
    }

    @InitBinder("accountType")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(accountTypeValidator);
    }


}
