package com.ecquaria.cloud.moh.iais.job;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author: yichen
 * @date time:2/25/2020 1:52 PM
 * @description:
 */
@Delegator("selfDeclNotificationDelegator")
@Slf4j
public class SelfDeclNotificationDelegator {

	@Autowired
	private ApplicationService applicationService;

	public void entry(BaseProcessClass bpc){
		applicationService.alertSelfDeclNotification();
	}
}
