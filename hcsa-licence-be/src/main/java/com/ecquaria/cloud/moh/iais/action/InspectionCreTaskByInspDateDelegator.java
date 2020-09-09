package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/21 15:07
 **/
@Delegator("inspectionCreTaskByInspDateDelegator")
@Slf4j
public class InspectionCreTaskByInspDateDelegator {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationService applicationService;

    /**
     * StartStep: mohCreTaskByInspecDateStart
     *
     * @param bpc
     * @throws
     */
    public void mohCreTaskByInspecDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohCreTaskByInspecDateStart start ...."));
    }

    /**
     * StartStep: mohCreTaskByInspecDatePre
     *
     * @param bpc
     * @throws
     */
    public void mohCreTaskByInspecDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohCreTaskByInspecDatePre start ...."));
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtoList = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION).getEntity();
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            for(ApplicationDto applicationDto : applicationDtoList){
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPremisesRecommendationDto != null) {
                    appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
                }
            }
        }
        if(IaisCommonUtils.isEmpty(appPremisesRecommendationDtos)){
            return;
        }
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(AppPremisesRecommendationDto aRecoDto:appPremisesRecommendationDtos){
            if(aRecoDto.getRecomInDate() != null && aRecoDto.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
                Date today = new Date();
                String inspecDateStr = Formatter.formatDateTime(aRecoDto.getRecomInDate(), Formatter.DATE);
                String todayStr = Formatter.formatDateTime(today, Formatter.DATE);
                if(todayStr.equals(inspecDateStr) || today.after(aRecoDto.getRecomInDate())) {
                    ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(aRecoDto.getAppPremCorreId()).getEntity();
                    AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(aRecoDto.getAppPremCorreId()).getEntity();
                    if(InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION.equals(appInspectionStatusDto.getStatus())) {
                        log.debug(StringUtil.changeForLog("Current Application No. = " + applicationDto.getApplicationNo()));
                        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION, intranet);
                        applicationDto1.setAuditTrailDto(intranet);
                        applicationService.updateFEApplicaiton(applicationDto1);
                        updateInspectionStatus(aRecoDto.getAppPremCorreId(), InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY, intranet);
                    }
                }
            }
        }
    }

    private void updateInspectionStatus(String appPremCorreId, String status, AuditTrailDto intranet) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorreId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(intranet);
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus, AuditTrailDto intranet) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(intranet);
        return applicationViewService.updateApplicaiton(applicationDto);
    }
}
