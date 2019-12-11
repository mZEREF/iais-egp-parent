package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
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

    @Override
    public InspectionReportDto getInsRepDto(String appNo,ApplicationViewDto applicationViewDto) {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(appNo).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String appGrpId = applicationDto.getAppGrpId();
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        String licenseeId = applicationGroupDto.getLicenseeId();
        Integer isPre = applicationGroupDto.getIsPreInspection();
        String appType = MasterCodeUtil.getCodeDesc(appTypeCode);
        String reasonForVisit="";
        if(isPre==1){
            reasonForVisit = "Pre-licensing inspection for"+appType+" Application";
        }else {
            reasonForVisit = "Post-licensing inspection for"+appType+" Application";
        }

        List<String> list = new ArrayList<>();
        list.add(appInsRepDto.getServiceId());
        List<HcsaServiceDto> entity = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        String svcCode = "";
        for(HcsaServiceDto hcsaServiceDto : entity){
             svcName = hcsaServiceDto.getSvcName();
             svcCode = hcsaServiceDto.getSvcCode();
        }
        List<ChecklistQuestionDto> inspection = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        String configId = inspection.get(0).getConfigId();
        List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(configId, appPremisesCorrelationId);
        List<ReportNcRegulationDto> listReportNcRegulationDto = new ArrayList<>();
//        List<ReportNcRectifiedDto> listReportNcRectifiedDto = new ArrayList<>();
        List<String> ncItemId = new ArrayList<>();
        for (NcAnswerDto ncAnswerDto :ncAnswerDtoList){
            ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
            reportNcRegulationDto.setNc(ncAnswerDto.getItemId());
            reportNcRegulationDto.setRegulation(ncAnswerDto.getItemQuestion());
            listReportNcRegulationDto.add(reportNcRegulationDto);
            ncItemId.add(ncAnswerDto.getItemId());
        }
        //ReportNcRectifiedDto
        List<Boolean> ncRectified = insRepClient.isRectified(ncItemId).getEntity();

        for (int i = 0; i < ncRectified.size(); i++) {
//            ReportNcRectifiedDto reportNcRectifiedDto = new ReportNcRectifiedDto();
//            reportNcRectifiedDto.setNc(ncItemId.get(i));
//            reportNcRectifiedDto.setRectified(ncRectified.get(i));
//            listReportNcRectifiedDto.add(reportNcRectifiedDto);
        }

        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setPrincipalOfficer(appInsRepDto.getPrincipalOfficer());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        inspectionReportDto.setNcRegulation(listReportNcRegulationDto);
//        inspectionReportDto.setNcRectification(listReportNcRectifiedDto);
        






        inspectionReportDto.setInspectionDate(new Date());
        inspectionReportDto.setInspectionTime(new Date());
        inspectionReportDto.setStatus("Full Compliance");
        inspectionReportDto.setBestPractice("best practice!!!!");
        inspectionReportDto.setMarkedForAudit(true);
        inspectionReportDto.setInspectOffices("inspector officer");
        inspectionReportDto.setInspectorRemark("inspection Remark");
        inspectionReportDto.setTaskRemarks("taskRemake");
        inspectionReportDto.setReasonForVisit("pre inspection");
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
        return  applicationClient.updateApplication(applicationDto).getEntity();
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
