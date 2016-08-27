package com.gamesys.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gamesys.controller.Response.Status;
import com.gamesys.service.ExclusionService;


@RestController
@Validated
public class RegistrationController{

    public static final String DATE_OF_BIRTH_IS_NOT_VALID = "Date of birth is not valid";
    public static final String USER_ALREADY_REGISTERED = "User already registered";
    public static final String DATE_OF_BIRTH_MUST_BE_IN_THE_PAST = "Date of birth must be in the past";
    public static final String USER_IS_IN_BLACKLIST = "User is in blacklist";
    public static final String SUCCESSFULLY_REGISTERED = "Successfully registered";

    private final ConcurrentHashMap<String,Boolean> registeredUsers = new ConcurrentHashMap<>();    

    @Autowired ExclusionService exclusionService;

    
    /**
     * Having the request parameters in the HTTP Request-Body looked more simple and more proper for POST method. 
     */
    @PostMapping(value = "/register",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Response register(@Valid @NotNull @RequestBody final RegistrationRequest registrationRequest,BindingResult error) {

        final LocalDate dob;
        
        try {            
            dob = LocalDate.parse(registrationRequest.getDateOfBirth());
        } catch (DateTimeParseException e) {
            throw new RegistrationServiceException(DATE_OF_BIRTH_IS_NOT_VALID);
        }
        
        if(!dob.isBefore(LocalDate.now())){
            throw new RegistrationServiceException(DATE_OF_BIRTH_MUST_BE_IN_THE_PAST);
        }

        if(registeredUsers.containsKey(registrationRequest.getUsername())){
            throw new RegistrationServiceException(USER_ALREADY_REGISTERED);
        }

        if(!exclusionService.validate(registrationRequest.getDateOfBirth(), registrationRequest.getSsn())){
            throw new RegistrationServiceException(USER_IS_IN_BLACKLIST);
        }

        registeredUsers.put(registrationRequest.getUsername(), Boolean.TRUE);
        return new Response(Status.SUCCESS,SUCCESSFULLY_REGISTERED);
    }


    @ExceptionHandler(value = { RegistrationServiceException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleRegistrationServiceException(final RegistrationServiceException re) {
        return new Response(Status.ERROR,re.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleBedOrEmptyRequestBodyException(final HttpMessageNotReadableException httpe) {
        return new Response(Status.ERROR,httpe.getMessage());
    }
    
    
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleConstraintViolationException(final ConstraintViolationException e) {

        Response response = new Response(Status.ERROR);
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            response.getMessages().add(violation.getMessage());
        }
        return response;
    }

    
}
