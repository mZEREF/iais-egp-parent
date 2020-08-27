package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@JobHandler(value="approveWithdrawalJobHandler")
@Component
@Slf4j
public class AppealWdAppBatchjobHandler extends IJobHandler {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            List<ApplicationDto> applicationDtoList = applicationClient.saveWithdrawn().getEntity();
            if (applicationDtoList != null){
                applicationDtoList.forEach(h -> {
                    applicationService.updateFEApplicaiton(h);
                });
                log.error("**** The withdraw Application List size"+applicationDtoList.size());
            }else{
                log.error("**** The withdraw Application List is null *****");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
