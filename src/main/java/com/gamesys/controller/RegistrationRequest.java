package com.gamesys.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegistrationRequest {

    public static final String SSN_PATTERN = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
    public static final String DATE_OF_BIRTH_PATTERN = "^(19|20)\\d\\d([- /.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])$";
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(.){4,40}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{1,40}$";
    public static final String PASSWORD_FORMAT_ERROR = "Password must include at least four characters, at least one upper case character and at least one number";
    public static final String SSN_FORMAT_IS_WRONG = "SSN format is wrong";
    public static final String SSN_IS_MANDATORY = "SSN is mandatory";
    public static final String DATE_OF_BIRTH_SHOULD_BE_IN_YYYY_MM_DD_FORMAT = "Date of birth should be in yyyy-MM-dd format";
    public static final String DATE_OF_BIRTH_IS_MANDATORY = "DateOfBirth is mandatory";
    public static final String PASSWORD_IS_MANDATORY = "Password is mandatory";
    public static final String USERNAME_SHOULD_BE_ALPHANUMERIC = "Username should be alphanumeric";
    public static final String USERNAME_IS_MANDATORY = "Username is mandatory";
    
    @NotNull(message = USERNAME_IS_MANDATORY)
    @Pattern(regexp=USERNAME_PATTERN, message = USERNAME_SHOULD_BE_ALPHANUMERIC)
    private String username;

    @NotNull(message = PASSWORD_IS_MANDATORY)
    @Pattern(regexp=PASSWORD_PATTERN, 
    message = PASSWORD_FORMAT_ERROR)
    private String password;

    @NotNull(message = DATE_OF_BIRTH_IS_MANDATORY)
    @Pattern(regexp = DATE_OF_BIRTH_PATTERN, message = DATE_OF_BIRTH_SHOULD_BE_IN_YYYY_MM_DD_FORMAT)
    private String dateOfBirth;

    @NotNull(message = SSN_IS_MANDATORY)
    @Pattern(regexp=SSN_PATTERN, message = SSN_FORMAT_IS_WRONG)
    private String ssn;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RegistrationRequest [username=").append(username).
        append(", password=").append(password).
        append(", dateOfBirth=").append(dateOfBirth).
        append(", ssn=").append(ssn).append("]");
        return builder.toString();
    }

}
