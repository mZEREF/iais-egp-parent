package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wenkang
 * @date 2020/9/23 15:49
 */
@JobHandler(value="autoRenwalDelegatorHander")
@Component
@Slf4j
public class AutoRenwalDelegatorHander extends IJobHandler {
    @Autowired
    private AutoRenwalService autoRenwalService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            autoRenwalService.startRenwal();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
