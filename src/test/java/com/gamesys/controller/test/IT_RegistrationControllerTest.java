package com.gamesys.controller.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gamesys.config.web.AppConfig;
import com.gamesys.controller.RegistrationController;
import com.gamesys.model.Registration;
import com.gamesys.model.Response;
import com.gamesys.model.Response.Status;

/*
 * Integration Tests
 * */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class IT_RegistrationControllerTest extends RegistrationTest{

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RegistrationController registrationController;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSuccessfulRegistration() throws Exception{
        
        String request = getJson(createRegistrationRequest());
        String response = mockMvc.perform(
                post("/register").content(request).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertEquals("Response is not as expected",SUCCESS_RESPONSE, response);
        Assert.assertNotNull("USer Test is not registered successfully", registrationController.getRegisteredUser("Test"));
    }

    @Test
    public void testRegistrationValidation() throws Exception{
        
        Registration registrationRequest = createInValidRequest();
        registrationRequest.setUsername("");
        String request = getJson(registrationRequest);
        String response = mockMvc.perform(
                post("/register").content(request).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn().getResponse().getContentAsString();

        Response expectedResponse = new Response(Status.ERROR);
        expectedResponse.getMessages().add(Registration.DATE_OF_BIRTH_SHOULD_BE_IN_YYYY_MM_DD_FORMAT);
        expectedResponse.getMessages().add(Registration.SSN_FORMAT_IS_WRONG);
        expectedResponse.getMessages().add(Registration.USERNAME_SHOULD_BE_ALPHANUMERIC);
        expectedResponse.getMessages().add(Registration.PASSWORD_FORMAT_ERROR);
        
        Assert.assertEquals("Response is not as expected",expectedResponse, getObjectFromJsonString(response,Response.class));
        Assert.assertNull("User with empty username should not have been registered", registrationController.getRegisteredUser(""));
    }

    @Test
    public void testRegistrationServiceExceptionHandling() throws Exception{
        
        Registration registrationRequest = createRegistrationRequest();
        registrationRequest.setUsername("DuplicateUser");
        String request = getJson(registrationRequest);
        String response = mockMvc.perform(
                post("/register").content(request).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assert.assertEquals("Response is not as expected",SUCCESS_RESPONSE, response);
        Assert.assertNotNull("User DuplicateUser should have been registered", registrationController.getRegisteredUser("DuplicateUser"));

        String alreadyRegisteredResponse = mockMvc.perform(
                post("/register").content(request).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn().getResponse().getContentAsString();

        
        Response expectedResponse = new Response(Status.ERROR);
        expectedResponse.getMessages().add(RegistrationController.USER_ALREADY_REGISTERED);
        
        Assert.assertEquals("Response is not as expected",expectedResponse, getObjectFromJsonString(alreadyRegisteredResponse,Response.class));
    }

    
}
