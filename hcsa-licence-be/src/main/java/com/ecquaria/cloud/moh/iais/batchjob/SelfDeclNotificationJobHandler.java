package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: yichen
 * @date time:2/25/2020 1:52 PM
 * @description:
 */
@JobHandler(value="selfDeclNotificationJobHandler")
@Component
@Slf4j
public class SelfDeclNotificationJobHandler extends IJobHandler {

	@Autowired
	private ApplicationService applicationService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		applicationService.alertSelfDeclNotification();
		return ReturnT.SUCCESS;
	}
}
