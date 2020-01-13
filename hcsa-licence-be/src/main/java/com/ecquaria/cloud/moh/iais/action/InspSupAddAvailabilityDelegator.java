package com.ecquaria.cloud.moh.iais.action;

/**
 * @Process: MohInspSupAddAvailability
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Delegator("inspSupAddAvailabilityDelegator")
@Slf4j
public class InspSupAddAvailabilityDelegator {

    @Autowired
    private InspSupAddAvailabilityService inspSupAddAvailabilityService;

    @Autowired
    private InspSupAddAvailabilityDelegator(InspSupAddAvailabilityService inspSupAddAvailabilityService){
        this.inspSupAddAvailabilityService = inspSupAddAvailabilityService;
    }


}
