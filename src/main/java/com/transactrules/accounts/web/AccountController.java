package com.transactrules.accounts.web;

import com.transactrules.accounts.metadata.AccountType;
import com.transactrules.accounts.runtime.Account;
import com.transactrules.accounts.runtime.CodeGenService;
import com.transactrules.accounts.runtime.Transaction;
import com.transactrules.accounts.services.*;
import com.transactrules.accounts.utilities.Utility;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    @Autowired
    private AccountValidator accountValidator;

    @Autowired
    AccountTypeService accountTypeService;

    @Autowired
    UniqueIdService uniqueIdService;

    @Autowired
    CodeGenService codeGenService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    AccountService accountService;

    @Autowired
    SystemPropertyService properties;

    @RequestMapping(value="/{accountTypeName}/new", method = RequestMethod.GET)
    @ApiOperation(value = "Get default account data", response = Account.class)
    public ResponseEntity<?> create(@PathVariable(required = true) String accountTypeName ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (Utility.isEmpty(accountTypeName))
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        AccountType accountType = accountTypeService.findByClassName(accountTypeName);

        if(accountType == null){
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

        Account account = accountService.create(accountType);

        return new ResponseEntity<>(account, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping( path = "/calculateProperties", method = RequestMethod.POST)
    @ApiOperation(value = "Get calculated properties based on input prototype properties", response = Account.class)
    public ResponseEntity<?> getCalculatedProperties(@Valid @RequestBody Account prototype) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (prototype == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Account calculatedAccount = accountService.calculateProperties(prototype);

        return new ResponseEntity<>(calculatedAccount, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping( path = "/solveInstalments", method = RequestMethod.POST)
    @ApiOperation(value = "Solve instalments", response = Account.class)
    public ResponseEntity<?> solveInstalments(@Valid @RequestBody Account prototype) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (prototype == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Account calculatedAccount;

        try{
             calculatedAccount = accountService.calculateInstalments(prototype);
        }
        catch(ArithmeticException aex){
            List<ApiGlobalError> apiGlobalErrors = new ArrayList<>();
            apiGlobalErrors.add(new ApiGlobalError(ApiErrorCode.NO_SOLUTIONS_FOUND.getCode(), ApiErrorCode.NO_SOLUTIONS_FOUND.getDescription()));
            ApiErrorsView apiErrorsView = new ApiErrorsView(null, apiGlobalErrors);

            return new ResponseEntity<>(apiErrorsView, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        return new ResponseEntity<>(calculatedAccount, httpHeaders, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save new Account")
    public ResponseEntity<?> save(@Valid @RequestBody Account account ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (account == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Account savedItem = service.save(account);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedItem.getAccountNumber()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all Accounts", response = Account.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<Account> accounts = service.findAll();

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation(value = "Get Account by Account Number", response = Account.class)
    public ResponseEntity<?> findByAccountNumber( @PathVariable("id") String accountNumber){
        Account account = service.findByAccountNumber(accountNumber);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/transactions", method= RequestMethod.GET)
    @ApiOperation(value = "Get Transactions for account", response = Transaction.class, responseContainer = "List")
    public ResponseEntity<?> findByAccountNumber( @PathVariable("id") String accountNumber,
                                                  @RequestParam(value="from", defaultValue="today") String from,
                                                  @RequestParam(value="to", defaultValue="today") String to ){

        LocalDate fromDate = toDate(from);
        LocalDate toDate = toDate(to);

        List<Transaction> transactions=  service.findTransactions(accountNumber, fromDate, toDate);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    private LocalDate toDate(String value){
        if(value.equalsIgnoreCase("today")){
            return  properties.getActionDate();
        }

        return LocalDate.parse(value);
    }

    @RequestMapping(value = "/{id}/activate", method= RequestMethod.GET)
    @ApiOperation(value = "Activate account", response = Account.class)
    public ResponseEntity<?> activate( @PathVariable("id") String accountNumber){
        Account prototype = service.findByAccountNumber(accountNumber);

        Account activatedAccount =  accountService.activate(prototype);

        return new ResponseEntity<>(activatedAccount, HttpStatus.OK);
    }

    @InitBinder("account")
    public void setupBinder(WebDataBinder binder) {

        binder.addValidators(accountValidator);
    }
}
