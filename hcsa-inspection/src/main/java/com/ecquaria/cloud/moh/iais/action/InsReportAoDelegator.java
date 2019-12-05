package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weilu
 * @date 2019/12/5 13:16
 */
@Delegator(value = "insReportAo")
@Slf4j
public class InsReportAoDelegator {

    @Autowired
    private InsRepService insRepService;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void reportInit(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>inspectionReportInit");
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
    }


    public void reportPre(BaseProcessClass bpc) {
        log.info("=======cc>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
        String appNo = "AN1911153344-01";
        InspectionReportDto insRepDto = insRepService.getInsRepDto(appNo);
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        SelectOption so1 = new SelectOption("Reject","Reject");
        SelectOption so2 = new SelectOption("1Y","1year");
        SelectOption so3 = new SelectOption("2Y","2year");
        List<SelectOption> inspectionReportTypeOption = new ArrayList<>();
        inspectionReportTypeOption.add(so1);
        inspectionReportTypeOption.add(so2);
        inspectionReportTypeOption.add(so3);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    public void approveReport(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the approveReport start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        String Remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        Integer version = 1;
        String status = "Pending";
        appPremisesRecommendationDto.setRemarks(Remarks);
        appPremisesRecommendationDto.setRecomType(recommendation);
        appPremisesRecommendationDto.setStatus(status);
        appPremisesRecommendationDto.setVersion(version);
        appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        Boolean isSuccess = insRepService.saveRecommendation(appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
    }

    public void rejectReport(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the rejectReport start ...."));

    }


}
