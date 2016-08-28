package com.gamesys.controller.test;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;
import org.junit.Test;

import com.gamesys.model.Registration;

public class ValidationRegexTest {

    Matcher<String> userNameMatchesPattern = MatchesPattern.matchesPattern(Registration.USERNAME_PATTERN);
    Matcher<String> passwordMatchesPattern = MatchesPattern.matchesPattern(Registration.PASSWORD_PATTERN);
    Matcher<String> dateOfBirthMatchesPattern = MatchesPattern.matchesPattern(Registration.DATE_OF_BIRTH_PATTERN);
    Matcher<String> ssnMatchesPattern = MatchesPattern.matchesPattern(Registration.SSN_PATTERN);

    @Test
    public void testValidUserNamePattern(){
        Assert.assertThat("Test123ab2c", userNameMatchesPattern);
    }

    @Test
    public void testUsernameShouldNotBeValidWhenHavingSpaces(){
        Assert.assertThat("\"A10 23\" is not alphanumeric","A10 23", IsNot.not(userNameMatchesPattern));
    }
    
    @Test
    public void testUsernameShouldNotBeValidWhenHavingMinus(){
        Assert.assertThat("A10-23 is not alphanumeric","A10-23", IsNot.not(userNameMatchesPattern));
    }

    @Test
    public void testUsernameShouldNotBeValidWhenEmpty(){
        Assert.assertThat("Empty string should not be valid","", IsNot.not(userNameMatchesPattern));
    }
    
    @Test
    public void testUsernameShouldNotBeValidWhenLongerThan40Characters(){
        Assert.assertThat("Username should not be longer than 40 character","12345678901234567890123456789012345678903", IsNot.not(userNameMatchesPattern));        
    }
    
    @Test
    public void testValidPassword(){
        Assert.assertThat("Test123ab2c", passwordMatchesPattern);        
    }

    /**
     * Since it is not mentioned i have to accept spaces in the password
     */
    @Test    
    public void testValidPasswordWithSpace(){
        Assert.assertThat("\"A10 23\" is not alphanumeric","A10 23", passwordMatchesPattern);
    }
    
    @Test
    public void testPasswordShouldNotBeValidWhenLongerThan40Characters(){
        Assert.assertThat("Password longer than 40 character should not be valid","123456789012345678901234567890123456789A1", IsNot.not(passwordMatchesPattern));
    }
    
    @Test
    public void testValidPasswordWithMinus(){
        Assert.assertThat("A10-abc23", passwordMatchesPattern);
    }

    @Test
    public void testPasswordShouldNotBeValidWhenLengthIsLessThan4(){
        Assert.assertThat("Should not be valid","A12", IsNot.not(passwordMatchesPattern));
    }
    
    @Test
    public void testPasswordShouldNotBeValidWhenEmpty(){
        Assert.assertThat("Empty string should not be valid","", IsNot.not(passwordMatchesPattern));
    }

    
    @Test
    public void testPasswordShouldNotBeValidWhenNoUpperCaseCharExists(){
        Assert.assertThat("abc123 should not be valid","abc123", IsNot.not(passwordMatchesPattern));
    }

    @Test
    public void testPasswordShouldNotBeValidWhenNoNumberExists(){
        Assert.assertThat("Abvc should not be valid","Abvc", IsNot.not(passwordMatchesPattern));
    }
    
    @Test
    public void testValidDateOFBirthPattern(){
        Assert.assertThat("1968-12-12", dateOfBirthMatchesPattern);
    }

    @Test
    public void testValidDateOFBirthPatternWithLeadingZeros(){
        Assert.assertThat("1998-02-02", dateOfBirthMatchesPattern);        
    }

    @Test
    public void testDateOfBirthShouldNotBeValidWhenEmpty(){
        Assert.assertThat("Empty string should not be valid","", IsNot.not(dateOfBirthMatchesPattern));
    }

    @Test
    public void testDateOfBirthShouldNotBeValidWhenDDMMYYYY(){
        Assert.assertThat("22-12-1989 Should not be valid","22-12-1989", IsNot.not(dateOfBirthMatchesPattern));
    }

    @Test
    public void testDateOfBirthShouldNotBeValidWhenYYYYYMMDD(){
        Assert.assertThat("12222-12-12 should not be valid","12222-12-12", IsNot.not(dateOfBirthMatchesPattern));
    }
     
    @Test
    public void testValidSsnStartsWith6(){
        Assert.assertThat("668-12-9999", ssnMatchesPattern);        
    }

    @Test
    public void testValidSsnStartsWith7(){
        Assert.assertThat("768-12-1223", ssnMatchesPattern);
    }

    @Test
    public void testValidSsnStartsWith8(){
        Assert.assertThat("898-02-0002", ssnMatchesPattern);
    }

    @Test
    public void testSsnShouldNotBeValidWhen698Minus02Minus02(){
        Assert.assertThat("698-02-02 should not be valid","998-02-02", IsNot.not(ssnMatchesPattern));
    }

    @Test
    public void testSsnShouldNotBeValidWhen1998Minus02Minus0002(){
        Assert.assertThat("1998-02-0002 should not be valid","1998-02-0002", IsNot.not(ssnMatchesPattern));
    }

    @Test
    public void testSsnShouldNotBeValidWhen998Minus02Minus902(){
        Assert.assertThat("998-02-902 should not be valid","998-02-902", IsNot.not(ssnMatchesPattern));
    }

    @Test
    public void testSsnShouldNotBeValidWhen0Minus02Minus1902(){
        Assert.assertThat("0-02-1902 should not be valid","0-02-1902", IsNot.not(ssnMatchesPattern));
    }

    @Test
    public void testSsnShouldNotBeValidWhen000Minus02Minus1902(){
        Assert.assertThat("000-02-1902 should not be valid","000-02-1902", IsNot.not(ssnMatchesPattern));
    }

    
    @Test
    public void testSsnShouldNotBeValidWhen99802Minus0002(){
        Assert.assertThat("99802-0002 should not be valid","99802-0002", IsNot.not(ssnMatchesPattern));
    }
    
    @Test
    public void testSsnShouldNotBeValidWhenStartsWith9(){
        Assert.assertThat("998-02-0002 should not be valid","998-02-0002", IsNot.not(ssnMatchesPattern));
    }

}
