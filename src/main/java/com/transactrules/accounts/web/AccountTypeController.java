package com.transactrules.accounts.web;

import com.transactrules.accounts.configuration.AccountType;
import com.transactrules.accounts.services.AccountTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(path="/accountTypes")
public class AccountTypeController {

    private final AccountTypeService service;

    @Autowired
    public AccountTypeController(AccountTypeService service){
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Add a new AccountType")
    public ResponseEntity<?> create(@RequestBody AccountTypeDto item ){
        HttpHeaders httpHeaders = new HttpHeaders();

        if (item == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        final AccountType savedItem = service.create(item.toAccountType());

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedItem.getId()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all AccountTypes", response = AccountType.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<AccountType> accountTypes = service.findAll();

        return new ResponseEntity<>(accountTypes, HttpStatus.OK);
    }




}
