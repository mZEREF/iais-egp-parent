package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

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
    @Autowired
    private CessationClient cessationClient;

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
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if(licenceDtos!=null&&!licenceDtos.isEmpty()){
            for(LicenceDto licenceDto :licenceDtos){
                String id = licenceDto.getId();
                ids.clear();
                ids.add(id);
                List<String> entity = cessationClient.getlicIdToCessation(ids).getEntity();
                if(entity!=null&&!entity.isEmpty()){
                    licenceDtosForSave.add(licenceDto);
                }
            }
        }
        List<LicenceDto> licenceDtos2 = updateLicenceStatus(licenceDtosForSave,date);
        hcsaLicenceClient.updateLicences(licenceDtos2).getEntity();


        //cessation application and licence
        String type = ApplicationConsts.CESSATION_TYPE_APPLICATION;
        //get misc corrId
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPreCorrDtos(type, dateStr).getEntity();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
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
                String licenceId = applicationDto.getOriginLicenceId();
                licIds.add(licenceId);
            }
        }
        List<String> entity = cessationClient.getlicIdToCessation(licIds).getEntity();

        //update application
        List<ApplicationDto> updateStatusApplicationDtos = updateApplicationStatus(applicationDtos);
        applicationClient.updateCessationApplications(updateStatusApplicationDtos).getEntity();
        //update licence
        List<LicenceDto> licenceDtoApps = hcsaLicenceClient.retrieveLicenceDtos(licIds).getEntity();
        List<LicenceDto> licenceDtos1 = updateLicenceStatus(licenceDtoApps,date);
        hcsaLicenceClient.updateLicences(licenceDtos1).getEntity();


    }




    private List<ApplicationDto> updateApplicationStatus(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> updateApplications = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto :applicationDtos){
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            updateApplications.add(applicationDto);
        }
        return updateApplications;
    }

    private List<LicenceDto> updateLicenceStatus(List<LicenceDto> licenceDtos,Date date){
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        for(LicenceDto licenceDto :licenceDtos){
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);
        }
        return updateLicenceDtos;
    }
}
