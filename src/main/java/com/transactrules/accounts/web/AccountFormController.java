package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.services.AccountTypeService;
import com.transactrules.accounts.utilities.Utilities;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping(path="/accountOpen")
public class AccountFormController {

    @Autowired
    AccountFormFactory processFactory;

    @Autowired
    AccountTypeService accountTypeService;

    @RequestMapping(value="/{accountTypeName}/", method = RequestMethod.GET)
    @ApiOperation(value = "Get Account Form", response = AccountForm.class)
    public ResponseEntity<?> create(@PathVariable(required = true) String accountTypeName ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (Utilities.isEmpty(accountTypeName))
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        AccountType accountType = accountTypeService.findByClassName(accountTypeName);

        if(accountType == null){
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

        AccountForm accountForm = processFactory.createAccountForm(accountType);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(accountForm).toUri());

        return new ResponseEntity<>(accountForm, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping( path = "/{accountTypeName}/calculatedFields", method = RequestMethod.POST)
    @ApiOperation(value = "Get calculated fields based on account details", response = AccountForm.class)
    public ResponseEntity<?> getSchedules(@PathVariable("accountTypeName") String accountTypeName, @RequestBody Map<String, Object> accountProperties ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (accountProperties == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        AccountType accountType = accountTypeService.findByClassName(accountTypeName);

        if(accountType == null){
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

        AccountForm accountForm = processFactory.createAccountForm(accountType);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(accountForm).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }


}
