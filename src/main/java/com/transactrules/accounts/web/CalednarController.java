package com.transactrules.accounts.web;

import com.transactrules.accounts.runtime.Calendar;
import com.transactrules.accounts.services.CalendarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/calendars")
public class CalednarController {

    @Autowired
    CalendarService service;

    @RequestMapping(method= RequestMethod.GET)
    @ApiOperation(value = "Get all Calebdars", response = Calendar.class, responseContainer="List")
    public ResponseEntity<?> findAll(){
        List<Calendar> accounts = service.findAll();

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}
