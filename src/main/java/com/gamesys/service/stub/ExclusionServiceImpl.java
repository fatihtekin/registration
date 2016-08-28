package com.gamesys.service.stub;

import org.springframework.stereotype.Service;

import com.gamesys.service.ExclusionService;

@Service
public class ExclusionServiceImpl implements ExclusionService{

    @Override
    public boolean validate(final String dob,final String ssn) {

        if(dob == null || ssn == null || dob.length() != 10 || ssn.length() != 11){
            return false;
        }

        final boolean isDayofDOBOddNumber = Integer.valueOf(dob.substring(8,10))%2 == 1;
        final boolean islast4digitsOfSSNDivisibleBy13 = Integer.valueOf(ssn.substring(7,11))%13 == 0; 
        
        final boolean isBlackListed = isDayofDOBOddNumber && islast4digitsOfSSNDivisibleBy13;
        
        return !isBlackListed;
    }

}
