package com.gamesys.controller.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamesys.model.Registration;

public  abstract class RegistrationTest {

    protected final static String SUCCESS_RESPONSE = "{\"messages\":[\"Successfully registered\"],\"status\":\"SUCCESS\"}";
    
    protected String getJson(Object object) {
        ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getObjectFromJsonString(String json, Class<T> type) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json.getBytes(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected Registration createRegistrationRequest() {
        Registration registrationRequest = new Registration();
        registrationRequest.setDateOfBirth("1988-11-11");
        registrationRequest.setPassword("Tr12");
        registrationRequest.setSsn("786-45-2345");
        registrationRequest.setUsername("Test");
        return registrationRequest;
    }

    protected Registration createInValidRequest() {
        Registration registrationRequest = new Registration();
        registrationRequest.setDateOfBirth("198-11-11");
        registrationRequest.setPassword("ar12");
        registrationRequest.setSsn("786-45-345");
        registrationRequest.setUsername("");
        return registrationRequest;
    }
    
    protected Registration createBlackListedRegistrationRequest() {
        Registration registrationRequest = new Registration();
        registrationRequest.setDateOfBirth("1988-11-11");
        registrationRequest.setPassword("Tr12");
        registrationRequest.setSsn("786-45-0130");
        registrationRequest.setUsername("Test");
        return registrationRequest;
    }    

}
