package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.client.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/20 16:11
 */
@Service
@Slf4j
public class InsRepServiceImpl implements InsRepService {

    @Autowired
    private InsRepClient insRepClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;


    @Override
    public InspectionReportDto getInsRepDto(String appNo, ApplicationViewDto applicationViewDto) {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        //inspection report Dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(appNo).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();

        String appGrpId = applicationDto.getAppGrpId();
        //get application type (new/renew)
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        //String licenseeId = applicationGroupDto.getLicenseeId();
        //get application type (pre/post)
        Integer isPre = applicationGroupDto.getIsPreInspection();
        String appType = MasterCodeUtil.getCodeDesc(appTypeCode);
        String reasonForVisit = "";
        if (isPre == 1) {
            reasonForVisit = "Pre-licensing inspection for" + appType + " Application";
        } else {
            reasonForVisit = "Post-licensing inspection for" + appType + " Application";
        }
        //serviceId transform serviceCode
        List<String> list = new ArrayList<>();
        list.add(appInsRepDto.getServiceId());
        List<HcsaServiceDto> entity = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        String svcCode = "";
        for (HcsaServiceDto hcsaServiceDto : entity) {
            svcName = hcsaServiceDto.getSvcName();
            svcCode = hcsaServiceDto.getSvcCode();
        }
        //get configId
        List<ChecklistQuestionDto> listChecklistQuestionDtos = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        String configId = listChecklistQuestionDtos.get(0).getConfigId();

        List<ReportNcRegulationDto> listReportNcRegulationDto = new ArrayList<>();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = new ArrayList<>();
        //add ReportNcRegulationDto and add ncItemId
        List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(configId, appPremisesCorrelationId);
        if(ncAnswerDtoList!=null && ncAnswerDtoList.size()>=0){
            for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                reportNcRegulationDto.setRegulation(ncAnswerDto.getClause());
                listReportNcRegulationDto.add(reportNcRegulationDto);
            }
            inspectionReportDto.setStatus("Partial Compliance");

        }else {
            inspectionReportDto.setStatus("Full Compliance");
        }

        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationId).getEntity();
        String ncId = appPremPreInspectionNcDto.getId();
        List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
        for (AppPremisesPreInspectionNcItemDto preInspNc : listAppPremisesPreInspectionNcItemDtos) {
            ChecklistItemDto cDto = hcsaChklClient.getChklItemById(preInspNc.getItemId()).getEntity();
            ReportNcRectifiedDto reportNcRectifiedDto = new ReportNcRectifiedDto();
            reportNcRectifiedDto.setNc(cDto.getChecklistItem());
            reportNcRectifiedDto.setRectified(preInspNc.getIsRecitfied() == 1 ? "Yes" : "No");
            listReportNcRectifiedDto.add(reportNcRectifiedDto);
        }

        AppPremisesRecommendationDto recommendationDto = insRepClient.getRecommendationDto(appPremisesCorrelationId, "tcu").getEntity();
        String bestPractice = recommendationDto.getBestPractice();
        String remarks = recommendationDto.getRemarks();
        ChecklistConfigDto otherChecklist = hcsaChklClient.getMaxVersionConfigByParams("CLB", "Inspection", "New").getEntity();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = new ArrayList<>();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        String dateType = listRiskResultDto.get(0).getDateType();
        String count = String.valueOf(listRiskResultDto.get(0).getTimeCount());
        String recommTime = count+dateType;

        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setPrincipalOfficer(appInsRepDto.getPrincipalOfficer());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        inspectionReportDto.setNcRegulation(listReportNcRegulationDto);
        inspectionReportDto.setNcRectification(listReportNcRectifiedDto);
        inspectionReportDto.setOtherCheckList(otherChecklist);
        inspectionReportDto.setInspectionDate(new Date());
        inspectionReportDto.setInspectionTime(new Date());
        inspectionReportDto.setBestPractice(bestPractice);
        inspectionReportDto.setMarkedForAudit(true);
        inspectionReportDto.setInspectOffices("inspector officer");
        inspectionReportDto.setInspectorRemark("inspection Remark");
        inspectionReportDto.setTaskRemarks(remarks);
        inspectionReportDto.setReportedBy("weilu");
        inspectionReportDto.setReportNoteBy("jinhua");
        inspectionReportDto.setInspectedBy(inspects());
        return inspectionReportDto;
    }

    @Override
    public Boolean saveRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        try {
            Integer version = 1;
            appPremisesRecommendationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01);
            appPremisesRecommendationDto.setVersion(version);
            insRepClient.saveData(appPremisesRecommendationDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            return false;
        }
        return true;

    }

    @Override
    public ApplicationViewDto getApplicationViewDto(String appNo) {
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByNo(appNo).getEntity();
        return applicationViewDto;
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }


    private List<ReportNcRegulationDto> ncRegulation() {
        List<ReportNcRegulationDto> list = new ArrayList<>();
        ReportNcRegulationDto reportNcRegulationDto1 = new ReportNcRegulationDto();
        ReportNcRegulationDto reportNcRegulationDto2 = new ReportNcRegulationDto();
        ReportNcRegulationDto reportNcRegulationDto3 = new ReportNcRegulationDto();
        reportNcRegulationDto1.setNc("Nc1");
        reportNcRegulationDto2.setNc("Nc2");
        reportNcRegulationDto3.setNc("Nc3");
        reportNcRegulationDto1.setRegulation("regulation1");
        reportNcRegulationDto2.setRegulation("regulation2");
        reportNcRegulationDto3.setRegulation("regulation3");
        list.add(reportNcRegulationDto1);
        list.add(reportNcRegulationDto2);
        list.add(reportNcRegulationDto3);
        return list;
    }

    private List<String> inspects() {
        List<String> list = new ArrayList<>();
        list.add("inspection1");
        list.add("inspection2");
        list.add("inspection3");
        return list;
    }
}
