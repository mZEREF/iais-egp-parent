package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @Author weilu
 * @Date 2020/7/20 17:30
 */
@Delegator("prepareCeaastion")
@Slf4j
public class PrepareCessation {

    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationClient applicationClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The prepareCeaastion is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("The prepareCeaastion is do ..."));
        List<ApplicationDto> applicationDtos = cessationClient.prepareCessation().getEntity();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
           for(ApplicationDto applicationDto : applicationDtos){
               String originLicenceId = applicationDto.getOriginLicenceId();
               String licenceId = hcsaLicenceClient.findNewestLicId(originLicenceId).getEntity();
               if(!StringUtil.isEmpty(licenceId)){
                   applicationDto.setOriginLicenceId(licenceId);
               }
           }
           applicationClient.updateCessationApplications(applicationDtos).getEntity();
        }

    }
}
