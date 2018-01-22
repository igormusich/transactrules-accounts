package com.transactrules.accounts.web;

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
public class AccountOpenController {

    @Autowired
    ProcessFactory processFactory;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create new Account Open process", response = Process.class)
    public ResponseEntity<?> create(@RequestBody AccountOpenCreateRequest request ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (request == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Process process = processFactory.createAccountOpenProcess(request);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(process).toUri());

        return new ResponseEntity<>(process, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping( path = "/{id}/schedules", method = RequestMethod.POST)
    @ApiOperation(value = "Get calculated schedules based on account details", response = Process.class)
    public ResponseEntity<?> getSchedules(@PathVariable("id") String processId, @RequestBody Map<String, Object> accountProperties ) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (accountProperties == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        //Process process = processFactory.createAccountOpenProcess(request);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(processId).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }


}
