package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author weilu
 * @date 2020/2/13 16:20
 */
@Delegator("CessationLicenceBatchJob")
@Slf4j
public class CessationLicenceBatchJob {

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    public void satrt(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String status = ApplicationConsts.LICENCE_STATUS_ACTIVE;
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(status, dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = new ArrayList<>();
        if(licenceDtos!=null&&!licenceDtos.isEmpty()){
            for(LicenceDto licenceDto :licenceDtos){
                licenceDto.setEndDate(date);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                licenceDtosForSave.add(licenceDto);
            }
        }
        List<LicenceDto> entity = hcsaLicenceClient.updateLicences(licenceDtosForSave).getEntity();
    }
}
