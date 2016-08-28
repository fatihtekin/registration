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

import com.gamesys.model.Registration;
import com.gamesys.model.Response;
import com.gamesys.model.Response.Status;
import com.gamesys.service.ExclusionService;


@RestController
@Validated
public class RegistrationController{

    public static final String DATE_OF_BIRTH_IS_NOT_VALID = "Date of birth is not valid";
    public static final String USER_ALREADY_REGISTERED = "User already registered";
    public static final String DATE_OF_BIRTH_MUST_BE_IN_THE_PAST = "Date of birth must be in the past";
    public static final String USER_IS_IN_BLACKLIST = "User is in blacklist";
    public static final String SUCCESSFULLY_REGISTERED = "Successfully registered";

    private final ConcurrentHashMap<String,Registration> registeredUsers = new ConcurrentHashMap<>();    

    @Autowired ExclusionService exclusionService;

    /**
     * Having the request parameters in the HTTP Request-Body looked more simple and more proper for POST method. 
     */
    @PostMapping(value = "/register",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Response register(@Valid @NotNull @RequestBody final Registration registration,final BindingResult error) {

        validateRequest(registration);
        registeredUsers.put(registration.getUsername(), registration);
        return new Response(Status.SUCCESS,SUCCESSFULLY_REGISTERED);
    }


    protected void validateRequest(final Registration registration) {
        final LocalDate dob;
        
        try {            
            dob = LocalDate.parse(registration.getDateOfBirth());
        } catch (DateTimeParseException e) {
            throw new RegistrationServiceValidationException(DATE_OF_BIRTH_IS_NOT_VALID,e);
        }
        
        if(!dob.isBefore(LocalDate.now())){
            throw new RegistrationServiceValidationException(DATE_OF_BIRTH_MUST_BE_IN_THE_PAST);
        }

        if(registeredUsers.containsKey(registration.getUsername())){
            throw new RegistrationServiceValidationException(USER_ALREADY_REGISTERED);
        }

        if(!exclusionService.validate(registration.getDateOfBirth(), registration.getSsn())){
            throw new RegistrationServiceValidationException(USER_IS_IN_BLACKLIST);
        }
    }

    public Registration getRegisteredUser(final String username){
        return registeredUsers.get(username);
    }

    @ExceptionHandler(value = { RegistrationServiceValidationException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleRegistrationServiceException(final RegistrationServiceValidationException validationException) {
        return new Response(Status.ERROR,validationException.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleBedOrEmptyRequestBodyException(final HttpMessageNotReadableException httpMessageNotReadableException) {
        return new Response(Status.ERROR,"Request body is not in the correct format");
    }
    
    
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = BAD_REQUEST)
    public Response handleConstraintViolationException(final ConstraintViolationException constraintViolationException) {

        Response response = new Response(Status.ERROR);
        for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
            response.getMessages().add(violation.getMessage());
        }
        return response;
    }

    
}
