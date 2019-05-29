package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

@Delegator(value = "masterCodeController")
public class MasterCodeController {

    @Autowired
    private MasterCode masterCode;

    @Autowired
    private MasterCodeService masterCodeService;

    public void getMasterCodeList(BaseProcessClass process){
        List<MasterCode> lc =  masterCodeService.getMasterCodeList();
    }

}
