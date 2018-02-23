package com.transactrules.accounts.metadata.web;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.metadata.domain.PositionType;
import com.transactrules.accounts.metadata.service.AccountTypeService;
import com.transactrules.accounts.utilities.Utility;
import com.transactrules.accounts.web.PositionTypePatch;
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
import java.util.Optional;

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

        service.save(item);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{propertyName}")
                .buildAndExpand(item.getClassName()).toUri());

        return new ResponseEntity<>(item, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all AccountTypes", response = AccountType.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<AccountType> accountTypes = service.findAll();

        return new ResponseEntity<>(accountTypes, HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}", method= RequestMethod.GET)
    @ApiOperation(value = "Get AccountType by class Name", response = AccountType.class)
    public ResponseEntity<?> findByName( @PathVariable("className") String className){
        AccountType accountType = service.findByClassName(className);

        return new ResponseEntity<>(accountType, HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}", method= RequestMethod.DELETE)
    @ApiOperation(value = "Delete AccountType by class Name")
    public ResponseEntity<?> deleteByName( @PathVariable("className") String className){
        service.deleteByClassName(className);

        return new ResponseEntity<>( HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}/positionTypes", method= RequestMethod.GET)
    @ApiOperation(value = "Get AccountType by class Name", response = PositionType.class,  responseContainer="List")
    public ResponseEntity<?> getPositionTypesForAccount( @PathVariable("className") String className){
        AccountType accountType = service.findByClassName(className);

        return new ResponseEntity<>(accountType.getPositionTypes(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}/positionTypes/{propertyName}", method= RequestMethod.GET)
    @ApiOperation(value = "Get AccountType by class Name", response = PositionType.class)
    public ResponseEntity<?> getPositionTypeForAccount( @PathVariable("className") String className, @PathVariable("propertyName") String propertyName){
        AccountType accountType = service.findByClassName(className);

        Optional<PositionType> positionType = accountType.getPositionTypeByName(propertyName);

        if(positionType.isPresent()==false){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(positionType, HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}/positionTypes/{propertyName}", method= RequestMethod.PATCH)
    @ApiOperation(value = "Get AccountType by class Name", response = PositionType.class)
    public ResponseEntity<?> updatePositionTypeForAccount(@Valid @RequestBody PositionTypePatch item, @PathVariable("className") String className, @PathVariable("propertyName") String propertyName){
        AccountType accountType = service.findByClassName(className);

        Optional<PositionType> optionalPositionType = accountType.getPositionTypeByName(propertyName);

        if(!optionalPositionType.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PositionType positionType = optionalPositionType.get();

        if(!Utility.isEmpty(item.labelName)){
            positionType.setLabelName(item.labelName);
        }

        service.save(accountType);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().build().toUri());

        return new ResponseEntity<>(positionType, HttpStatus.OK);
    }

    @RequestMapping(value = "/{className}/positionTypes", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new PositionType", response = PositionType.class )
    public ResponseEntity<?> createPositionType(@Valid @RequestBody PositionType item, @PathVariable("className") String className )  {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (item == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        AccountType accountType = service.findByClassName(className);

        accountType.getPositionTypes().add(item);

        service.save(accountType);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{propertyName}")
                .buildAndExpand(item.getPropertyName()).toUri());

        return new ResponseEntity<>(accountType, httpHeaders, HttpStatus.CREATED);
    }

    @InitBinder("accountType")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(accountTypeValidator);
    }


}
