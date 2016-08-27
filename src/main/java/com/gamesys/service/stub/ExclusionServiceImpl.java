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

        Integer day = Integer.valueOf(dob.substring(8,10));
        Integer last4digitsOfSSN = Integer.valueOf(ssn.substring(7,11)); 
        
        if(day % 2 == 1 && last4digitsOfSSN %13 == 0){
            return false;
        }
        
        return true;
    }

}
