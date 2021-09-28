package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;


/**
 * @author wangyu
 * @date 2021/7/9
 */
@Controller
@Slf4j
public class VehicleCommonController {

    @Autowired
    private ApplicationService applicationService;
    @Value("${easmts.vehicle.sperate.flag}")
    private String vehicleOpenFlag;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    private final static String RECOMMENDATION_DTO= "appPremisesRecommendationDto";

    private final static String RECOMMENDATION_SHOW = "appPremisesRecommendation_Show_Desc";
    public void setVehicleInformation(HttpServletRequest request, TaskDto taskDto, ApplicationViewDto applicationViewDto){
        //get vehicle flag
        String vehicleFlag = applicationService.getVehicleFlagToShowOrEdit(taskDto, vehicleOpenFlag, applicationViewDto);
        //get vehicleNoList for edit
        List<String> vehicleNoList = applicationService.getVehicleNoByFlag(vehicleFlag, applicationViewDto);
        //sort AppSvcVehicleDto List
        applicationService.sortAppSvcVehicleListToShow(vehicleNoList, applicationViewDto);
        ParamUtil.setSessionAttr(request,  HcsaLicenceBeConstant.APP_VEHICLE_NO_LIST, (Serializable) vehicleNoList);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG, vehicleFlag);
    }

    public void clearVehicleInformationSession(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,  HcsaLicenceBeConstant.APP_VEHICLE_NO_LIST, null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG, null);
        clearReportSession(request);
    }

    public void clearReportSession(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(request, RECOMMENDATION_SHOW, null);
    }

    public AppPremisesRecommendationDto initAoRecommendation(String correlationId, BaseProcessClass bpc, String appType){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            if(InspectionReportConstants.APPROVED.equals(recomDecision)||InspectionReportConstants.APPROVEDLTC.equals(recomDecision)){
                initRecommendationDto.setRemarks(reportRemarks);
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                String recommendationOnlyShowStr =  IaisCommonUtils.getRecommendationYears(recomInNumber);
                initRecommendationDto.setPeriod(recommendationOnlyShowStr);
                ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_SHOW, IaisCommonUtils.getRecommendationOnlyShowStr(recomInNumber));
            }
            if(InspectionReportConstants.REJECTED.equals(recomDecision)){
                initRecommendationDto.setPeriod(InspectionReportConstants.REJECTED);
            }
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                String recommendation = appPremisesRecommendationDto.getRecomDecision();
                if(InspectionReportConstants.RFC_APPROVED.equals(recommendation)){
                    initRecommendationDto.setPeriod(InspectionReportConstants.APPROVED);
                }
                if(InspectionReportConstants.RFC_REJECTED.equals(recommendation)){
                    initRecommendationDto.setPeriod(InspectionReportConstants.REJECTED);
                }
            }
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage ;
            if(StringUtil.isEmpty(remarks)){
                engage = "off";
            }else {
                engage = "on";
            }
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, initRecommendationDto);
        return initRecommendationDto;
    }

}
