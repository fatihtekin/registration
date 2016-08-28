package com.gamesys.controller;

public class RegistrationServiceValidationException extends RuntimeException{

    private static final long serialVersionUID = 5700974626842324744L;

    public RegistrationServiceValidationException(final String message) {
        super(message);    
    }

    public RegistrationServiceValidationException(final String message,Exception e) {
        super(message,e);    
    }

}
