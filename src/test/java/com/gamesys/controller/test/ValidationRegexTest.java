package com.gamesys.controller.test;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;
import org.junit.Test;

import com.gamesys.controller.RegistrationRequest;

public class ValidationRegexTest {

    @Test
    public void testUserNamePattern(){

        Matcher<String> matchesPattern = MatchesPattern.matchesPattern(RegistrationRequest.USERNAME_PATTERN);
        Assert.assertThat("Test123ab2c", matchesPattern);
        Assert.assertThat("Empty string should not be valid","", IsNot.not(matchesPattern));
        Assert.assertThat("A10-23 is not alphanumeric","A10-23", IsNot.not(matchesPattern));
        Assert.assertThat("\"A10 23\" is not alphanumeric","A10 23", IsNot.not(matchesPattern));
        Assert.assertThat("Username should not be longer than 40 character","12345678901234567890123456789012345678903", IsNot.not(matchesPattern));

    }

    
    @Test
    public void testPasswordPattern(){

        Matcher<String> matchesPattern = MatchesPattern.matchesPattern(RegistrationRequest.PASSWORD_PATTERN);
        Assert.assertThat("Test123ab2c", matchesPattern);
        Assert.assertThat("Empty string should not be valid","", IsNot.not(matchesPattern));
        Assert.assertThat("Should not be valid","abc123", IsNot.not(matchesPattern));
        Assert.assertThat("Should not be valid","Abvc", IsNot.not(matchesPattern));
        Assert.assertThat("Should not be valid","A12", IsNot.not(matchesPattern));
        Assert.assertThat("A10-abc23", matchesPattern);
        Assert.assertThat("\"A10 23\" is not alphanumeric","A10 23", matchesPattern);
        Assert.assertThat("Password longer than 40 character should not be valid","123456789012345678901234567890123456789A1", IsNot.not(matchesPattern));
    }

    @Test
    public void testDateOFBirthPattern(){

        Matcher<String> matchesPattern = MatchesPattern.matchesPattern(RegistrationRequest.DATE_OF_BIRTH_PATTERN);
        Assert.assertThat("1968-12-12", matchesPattern);
        Assert.assertThat("1998-02-02", matchesPattern);
        Assert.assertThat("Empty string should not be valid","", IsNot.not(matchesPattern));
        Assert.assertThat("22-12-1989 Should not be valid","22-12-1989", IsNot.not(matchesPattern));
        Assert.assertThat("12222-12-12 should not be valid","12222-12-12", IsNot.not(matchesPattern));
        Assert.assertThat("1968-23-23 should not be valid","1968-23-23", IsNot.not(matchesPattern));
    }


    @Test
    public void testSsnPattern(){

        Matcher<String> matchesPattern = MatchesPattern.matchesPattern(RegistrationRequest.SSN_PATTERN);
        Assert.assertThat("768-12-1223", matchesPattern);
        Assert.assertThat("668-12-9999", matchesPattern);
        Assert.assertThat("898-02-0002", matchesPattern);
        Assert.assertThat("998-02-0002 should not be valid","998-02-0002", IsNot.not(matchesPattern));
        Assert.assertThat("998-02-02 should not be valid","998-02-02", IsNot.not(matchesPattern));
        Assert.assertThat("99802-0002 should not be valid","99802-0002", IsNot.not(matchesPattern));
        Assert.assertThat("1998-02-0002 should not be valid","1998-02-0002", IsNot.not(matchesPattern));
        Assert.assertThat("998-02-902 should not be valid","998-02-902", IsNot.not(matchesPattern));
        Assert.assertThat("0-02-1902 should not be valid","0-02-1902", IsNot.not(matchesPattern));
        Assert.assertThat("000-02-1902 should not be valid","000-02-1902", IsNot.not(matchesPattern));
        Assert.assertThat("900-02-1902 should not be valid","900-02-1902", IsNot.not(matchesPattern));
    
    }

}
