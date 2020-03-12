package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
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
 * @author weilu
 * @date 2020/2/13 9:42
 * Check for Cessation Effective Date  when cessation date == new date make licence inactive
 */
@Delegator("CessationEffectiveDateBatchjob")
@Slf4j
public class CessationEffectiveDateBatchjob {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }
    public void doBatchJob(BaseProcessClass bpc){

        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        String status = ApplicationConsts.LICENCE_STATUS_ACTIVE;
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(status, dateStr).getEntity();
        List<LicenceDto> licenceDtosForSave = new ArrayList<>();
        if(licenceDtos!=null&&!licenceDtos.isEmpty()){
            for(LicenceDto licenceDto :licenceDtos){
                licenceDto.setEndDate(date);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDtosForSave.add(licenceDto);
            }
        }
        hcsaLicenceClient.updateLicences(licenceDtosForSave).getEntity();

        //cessation application and licence
        String type = ApplicationConsts.CESSATION_TYPE_APPLICATION;
        //get misc corrId
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPreCorrDtos(type, dateStr).getEntity();
        List<String> appIds = new ArrayList<>();
        List<String> licIds = new ArrayList<>();
        //get applicationIds
        if(appPremisesCorrelationDtos!=null&&!appPremisesCorrelationDtos.isEmpty()){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto :appPremisesCorrelationDtos){
                String applicationId = appPremisesCorrelationDto.getApplicationId();
                appIds.add(applicationId);
            }
        }
        //get licIds
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtosByIds(appIds).getEntity();
        if(applicationDtos!=null&&!applicationDtos.isEmpty()){
            for(ApplicationDto applicationDto :applicationDtos){
                String licenceId = applicationDto.getLicenceId();
                licIds.add(licenceId);
            }
        }
        //update application
        List<ApplicationDto> updateStatusApplicationDtos = updateApplicationStatus(applicationDtos);
        applicationClient.updateCessationApplications(updateStatusApplicationDtos).getEntity();
        //update licence
        List<LicenceDto> licenceDtoApps = hcsaLicenceClient.retrieveLicenceDtos(licIds).getEntity();
        List<LicenceDto> licenceDtos1 = updateLicenceStatus(licenceDtoApps);
        hcsaLicenceClient.updateLicences(licenceDtos1).getEntity();


    }

    private List<ApplicationDto> updateApplicationStatus(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> updateApplications = new ArrayList<>();
        for(ApplicationDto applicationDto :applicationDtos){
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            updateApplications.add(applicationDto);
        }
        return updateApplications;
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos){
        List<LicenceDto> updateLicenceDtos = new ArrayList<>();
        for(LicenceDto licenceDto :licenceDtos){
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
