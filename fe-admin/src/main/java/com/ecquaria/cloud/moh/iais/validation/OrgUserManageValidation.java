package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.dto.OrgUserDto;

import java.util.HashMap;
import java.util.Map;

public class OrgUserManageValidation {

    public boolean userValidation(OrgUserDto userDto){

        Map<String, String> errMap = new HashMap<>();

        if(userDto.getUenNo() == null || userDto.getUenNo().length() ==0){
            return false;
        }

        return true;
    }
}
