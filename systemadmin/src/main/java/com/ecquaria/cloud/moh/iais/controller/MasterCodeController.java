package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;

@Delegator(value = "masterCodeController")
public class MasterCodeController {

    @Autowired
    private MasterCode masterCode;

    @Autowired
    private MasterCodeService masterCodeService;

    public void getMasterCodeList(){
        throw new UnsupportedOperationException();
    }

}
