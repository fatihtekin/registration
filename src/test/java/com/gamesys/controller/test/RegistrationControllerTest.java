package com.gamesys.controller.test;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gamesys.controller.RegistrationController;
import com.gamesys.controller.RegistrationServiceValidationException;
import com.gamesys.model.Registration;
import com.gamesys.service.ExclusionService;


public class RegistrationControllerTest extends RegistrationTest{

    @InjectMocks
    private RegistrationController registrationController;
    
    @Mock
    private ExclusionService exclusionService;

    @Mock
    private ConcurrentHashMap<String, Boolean> registeredUsers;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGivenValidRequestThenRegisterSuccessfully(){
        Registration registration = createRegistrationRequest();
        Mockito.doReturn(Boolean.TRUE).when(exclusionService).validate(Mockito.matches(registration.getDateOfBirth()), Mockito.matches(registration.getSsn()));
        registrationController.register(registration,null);
        Registration registered = registrationController.getRegisteredUser(registration.getUsername());
        Assert.assertEquals("Registration could not be completed successfully", registration, registered);
        Mockito.verify(exclusionService, times(1)).validate(Mockito.matches(registration.getDateOfBirth()), Mockito.matches(registration.getSsn()));        
    }
    
    @Test
    public void testGivenDateOfBirthIsInTheFutureThenThrowException(){
        thrown.expect(RegistrationServiceValidationException.class);
        thrown.expectMessage(RegistrationController.DATE_OF_BIRTH_MUST_BE_IN_THE_PAST);
        Registration registrationRequest = createRegistrationRequest();
        registrationRequest.setDateOfBirth(LocalDate.now().plusDays(1).toString());
        registrationController.register(registrationRequest,null);
    }

    @Test
    public void testGivenNotValidDateOfBirthThenThrowException(){
        thrown.expect(RegistrationServiceValidationException.class);
        thrown.expectMessage(RegistrationController.DATE_OF_BIRTH_IS_NOT_VALID);
        Registration registrationRequest = createRegistrationRequest();
        registrationRequest.setDateOfBirth("1985-02-30");
        registrationController.register(registrationRequest,null);
    }
    
    @Test
    public void testGivenAlreadyRegisteredUserThenThrowException(){
        thrown.expect(RegistrationServiceValidationException.class);
        thrown.expectMessage(RegistrationController.USER_ALREADY_REGISTERED);
        Registration registrationRequest = createRegistrationRequest();
        Mockito.doReturn(Boolean.TRUE).when(exclusionService).validate(Mockito.matches(registrationRequest.getDateOfBirth()), Mockito.matches(registrationRequest.getSsn()));
        registrationController.register(registrationRequest,null);
        Mockito.verify(exclusionService, times(1)).validate(Mockito.matches(registrationRequest.getDateOfBirth()), Mockito.matches(registrationRequest.getSsn()));
        registrationController.register(registrationRequest,null);
    }
    
    @Test
    public void testGivenUserIsInBlackListThenThrowException(){
        thrown.expect(RegistrationServiceValidationException.class);
        thrown.expectMessage(RegistrationController.USER_IS_IN_BLACKLIST);
        Registration registrationRequest = createBlackListedRegistrationRequest();
        Mockito.doReturn(Boolean.FALSE).when(exclusionService).validate(Mockito.matches(registrationRequest.getDateOfBirth()), Mockito.matches(registrationRequest.getSsn()));
        Assert.assertEquals(Boolean.FALSE, exclusionService.validate(registrationRequest.getDateOfBirth(), registrationRequest.getSsn()));
        try {            
            registrationController.register(registrationRequest,null);
        } catch (RegistrationServiceValidationException rse) {
            Mockito.verify(exclusionService, times(2)).validate(Mockito.matches(registrationRequest.getDateOfBirth()), Mockito.matches(registrationRequest.getSsn()));            
            throw rse;
        }
    }
    
}
