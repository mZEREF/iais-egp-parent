package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-28 10:50
 **/
@Delegator(value = "appointmentDelegator")
@Slf4j
public class AppointmentDelegator {

    @Autowired
    IntranetUserClient intranetUserClient;


    public void prepareDate(BaseProcessClass bpc){

    }

}
