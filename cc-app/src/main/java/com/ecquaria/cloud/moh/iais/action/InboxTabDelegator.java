package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InboxTabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-10-15 17:47
 **/
@Delegator("inboxTabDelegator")
@Slf4j
public class InboxTabDelegator {

    @Autowired
    private InboxTabService inboxTabService;


    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        List<ApplicationDto> applicationDtos = inboxTabService.selectAll();
    }

}
