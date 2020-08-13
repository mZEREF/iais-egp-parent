package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ErrorMsgRefreshBatchjob
 *
 * @author Jinhua
 * @date 2020/7/30 13:32
 */
@JobHandler(value="backendMsgRefreshJobHandler")
@Component
@Slf4j
public class BackendMsgRefreshJobHandler extends IJobHandler {
    @Autowired
    private IaisSystemClient systemBeLicClient;

    @Override
    public ReturnT<String> execute(String s) {
        try{
            logInfo("<====== Start to refresh error msg ======>");
            List<MessageDto> list = systemBeLicClient.getMessagesToRefresh().getEntity();
            Map<String, String> map = IaisCommonUtils.genNewHashMap();
            if (!IaisCommonUtils.isEmpty(list)) {
                for (MessageDto mc : list) {
                    map.put(mc.getCodeKey(), mc.getMessage());
                    mc.setNeedFlush(false);
                }
                MessageUtil.loadMessages(map);
                systemBeLicClient.saveMessages(list);
            }
            logInfo("<====== End to refresh error msg ======>");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

    private void logInfo(String info) {
        log.info(StringUtil.changeForLog(info));
        JobLogger.log(StringUtil.changeForLog(info));
    }
}
