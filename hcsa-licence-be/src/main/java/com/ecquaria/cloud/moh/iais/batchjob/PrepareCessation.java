package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("The prepareCeaastion is do ..."));
        jobExecute();
    }
    public void jobExecute(){
        List<ApplicationDto> applicationDtos = cessationClient.prepareCessation().getEntity();
        if (!IaisCommonUtils.isEmpty(applicationDtos)) {

            for (ApplicationDto applicationDto : applicationDtos) {
                try {
                    String originLicenceId = applicationDto.getOriginLicenceId();
                    if (!StringUtil.isEmpty(originLicenceId)) {
                        String licenceId = hcsaLicenceClient.findNewestLicId(originLicenceId).getEntity();
                        log.error(StringUtil.changeForLog("==============" + licenceId + "==============="));
                        if (!StringUtil.isEmpty(licenceId)) {
                            applicationDto.setOriginLicenceId(licenceId);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
            applicationClient.updateCessationApplications(applicationDtos).getEntity();
        }
    }
}
