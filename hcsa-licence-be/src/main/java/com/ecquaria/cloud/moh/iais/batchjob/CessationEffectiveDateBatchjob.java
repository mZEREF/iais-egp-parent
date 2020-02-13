package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CessationEffectiveDateBatchjob
 * @author weilu
 * @date 2020/2/13 9:42
 */
@Delegator("CessationEffectiveDateBatchjob")
@Slf4j
public class CessationEffectiveDateBatchjob {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
        String type = "cessation";
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPreCorrDtosByCorrIds(type, dateStr).getEntity();
        List<String> appIds = new ArrayList<>();
        List<String> licIds = new ArrayList<>();
        if(appPremisesCorrelationDtos!=null&&!appPremisesCorrelationDtos.isEmpty()){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto :appPremisesCorrelationDtos){
                String applicationId = appPremisesCorrelationDto.getApplicationId();
                appIds.add(applicationId);
            }
        }
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtosByIds(appIds).getEntity();
        if(applicationDtos!=null&&!applicationDtos.isEmpty()){
            for(ApplicationDto applicationDto :applicationDtos){
                String licenceId = applicationDto.getLicenceId();
                licIds.add(licenceId);
            }
        }
        //update application
        List<ApplicationDto> updateStatusApplicationDtos = updateApplicationStatus(applicationDtos);
        List<ApplicationDto> updateApplications = applicationClient.updateCessationApplications(updateStatusApplicationDtos).getEntity();
        //update licence
        List<LicenceDto> licenceDtos = hcsaLicenceClient.retrieveLicenceDtos(licIds).getEntity();
        List<LicenceDto> licenceDtos1 = updateLicenceStatus(licenceDtos);
        List<LicenceDto> entity = hcsaLicenceClient.updateLicences(licenceDtos1).getEntity();


    }

    private List<ApplicationDto> updateApplicationStatus(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> updateApplications = new ArrayList<>();
        for(ApplicationDto applicationDto :applicationDtos){
            applicationDto.setStatus("Iactive");
            updateApplications.add(applicationDto);
        }
        return updateApplications;
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos){
        List<LicenceDto> updateLicenceDtos = new ArrayList<>();
        for(LicenceDto licenceDto :licenceDtos){
            licenceDto.setStatus("Iactive");
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
