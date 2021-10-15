package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.NotificateApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppGroupMiscService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * NotificationApplicationUpdateBatchjob
 *
 * @author suocheng
 * @date 5/13/2020
 */
@Delegator("notificationApplicationUpdateBatchjob")
@Slf4j
public class NotificationApplicationUpdateBatchjob {
    @Autowired
    private AppGroupMiscService appGroupMiscService;

    @Autowired
    private LicenceService licenceService;

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }
    public void jobExecute(){
        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob start ..."));
        List<NotificateApplicationDto> notificateApplicationDtoList = appGroupMiscService.getNotificateApplicationDtos();
        if(!IaisCommonUtils.isEmpty(notificateApplicationDtoList)){
            log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob notificateApplicationDtoList.size() -->:"+ notificateApplicationDtoList.size()));
            //get all LicAppCorrelationDtos
            List<String> applicationIds = getApplicationIds(notificateApplicationDtoList);
            List<LicAppCorrelationDto> licAppCorrelationDtoList = null;
            if(!IaisCommonUtils.isEmpty(applicationIds)){
                licAppCorrelationDtoList =  licenceService.getLicAppCorrelationDtosByApplicationIds(applicationIds);
            }
            //update every application group
            for(NotificateApplicationDto notificateApplicationDto : notificateApplicationDtoList){
                List<ApplicationDto> notificateApplicationDtos = notificateApplicationDto.getNotificateApplicationDtos();
                List<ApplicationDto> amendApplicationDtos = notificateApplicationDto.getAmendApplicationDtos();
                //newNotificateApplicationDto
                NotificateApplicationDto newNotificateApplicationDto = new NotificateApplicationDto();
                //AppGroupMiscDto
                AppGroupMiscDto appGroupMiscDto = notificateApplicationDto.getAppGroupMiscDto();
                appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                newNotificateApplicationDto.setAppGroupMiscDto(appGroupMiscDto);
                //newAmendApplicationDtos
                if(!IaisCommonUtils.isEmpty(notificateApplicationDtos)){
                    log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob notificateApplicationDtos.size() -->:"+ notificateApplicationDtos.size()));
                    List<ApplicationDto> newAmendApplicationDtos = IaisCommonUtils.genNewArrayList();
                    for(ApplicationDto applicationDto : notificateApplicationDtos){
                        String orignLicenceId = applicationDto.getOriginLicenceId();
                        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob orignLicenceId -->:"+ orignLicenceId));
                        String appId = applicationDto.getId();
                        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob appId -->:"+ appId));
                        LicAppCorrelationDto licAppCorrelationDto = getLicAppCorrelationDtoByAppId(licAppCorrelationDtoList,appId);
                        List<ApplicationDto> sameOrignApplications = getSameOrignLicenceidApplication(amendApplicationDtos,orignLicenceId);
                        if(!IaisCommonUtils.isEmpty(sameOrignApplications) && licAppCorrelationDto!= null){
                            for (ApplicationDto applicationDto1 : sameOrignApplications){
                                applicationDto1.setOriginLicenceId(licAppCorrelationDto.getLicenceId());
                                newAmendApplicationDtos.add(applicationDto1);
                            }
                        }

                    }
                    if(IaisCommonUtils.isNotEmpty(newAmendApplicationDtos)){
                        newNotificateApplicationDto.setAmendApplicationDtos(newAmendApplicationDtos);
                        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
                        newNotificateApplicationDto.setAuditTrailDto(auditTrailDto);
                        appGroupMiscService.saveNotificateApplicationDto(newNotificateApplicationDto);
                    }else{
                        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob the auto applicaiton do not Generate Licence -->:"
                                +appGroupMiscDto.getAppGrpId()));
                    }
                }
            }
        }else{
            log.info(StringUtil.changeForLog("do not have need update Nofificate application"));
        }

        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob end ..."));

    }
    private LicAppCorrelationDto getLicAppCorrelationDtoByAppId(List<LicAppCorrelationDto> licAppCorrelationDtoList,String appId){
        log.info(StringUtil.changeForLog("The getLicAppCorrelationDtoByAppId start ..."));
        LicAppCorrelationDto result = null;
        if(!IaisCommonUtils.isEmpty(licAppCorrelationDtoList) && !StringUtil.isEmpty(appId)){
            log.info(StringUtil.changeForLog("The getLicAppCorrelationDtoByAppId licAppCorrelationDtoList.size() -->:"+ licAppCorrelationDtoList.size()));
            for(LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtoList){
                if(appId.equals(licAppCorrelationDto.getApplicationId())){
                    result = licAppCorrelationDto;
                    break;
                }
            }
        }
        log.info(StringUtil.changeForLog("The getLicAppCorrelationDtoByAppId end ..."));
        return result;

    }

    private List<ApplicationDto> getSameOrignLicenceidApplication(List<ApplicationDto> amendApplicationDtos,String orignLicenceId){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        log.info(StringUtil.changeForLog("The getSameOrignLicenceidApplication start ..."));
         if(!IaisCommonUtils.isEmpty(amendApplicationDtos) && !StringUtil.isEmpty(orignLicenceId)){
             for(ApplicationDto applicationDto : amendApplicationDtos){
                 if(orignLicenceId.equals(applicationDto.getOriginLicenceId())){
                     result.add(applicationDto);
                 }
             }
         }
        log.info(StringUtil.changeForLog("The getSameOrignLicenceidApplication result.size() -->:"+ result.size()));
        log.info(StringUtil.changeForLog("The getSameOrignLicenceidApplication end ..."));
        return result;

    }

    private List<String> getApplicationIds(List<NotificateApplicationDto> notificateApplicationDtoList ){
        log.info(StringUtil.changeForLog("The getApplicationIds start ..."));
        List<String> result = IaisCommonUtils.genNewArrayList();
        for(NotificateApplicationDto notificateApplicationDto : notificateApplicationDtoList) {
            List<ApplicationDto> notificateApplicationDtos = notificateApplicationDto.getNotificateApplicationDtos();
            if(!IaisCommonUtils.isEmpty(notificateApplicationDtos)){
                log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob notificateApplicationDtos.size() -->:"+ notificateApplicationDtos.size()));
                for(ApplicationDto applicationDto : notificateApplicationDtos){
                    String id = applicationDto.getId();
                    result.add(id);
                }
            }
        }
        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob result.size() -->:"+ result.size()));
        log.info(StringUtil.changeForLog("The getApplicationIds end ..."));
        return result;
    }
}
