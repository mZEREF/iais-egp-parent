package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author weilu
 * @Date 2020/7/20 17:30
 */
@Delegator("prepareCeaastion")
@Slf4j
public class PrepareCessation {

    @Autowired
    private CessationClient cessationClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The prepareCeaastion is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("The prepareCeaastion is do ..."));
        cessationClient.prepareCessation();
    }
}
