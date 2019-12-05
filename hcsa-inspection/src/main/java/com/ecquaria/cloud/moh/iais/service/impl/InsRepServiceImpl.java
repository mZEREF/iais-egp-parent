package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author weilu
 * date 2019/11/20 16:11
 */
@Service
@Slf4j
public class InsRepServiceImpl implements InsRepService {

    @Autowired
    private InsRepClient insRepClient;

    @Override
    public InspectionReportDto getInsRepDto(String appNo) {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(appNo).getEntity();
        inspectionReportDto.setServiceName(appInsRepDto.getServiceId());
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setPrincipalOfficer(appInsRepDto.getPrincipalOfficer());
        inspectionReportDto.setSubsumedService(appInsRepDto.getSubsumedService());

        //String licenceId = "92B33E39-B7FA-428A-901A-3AC7F8886F8C";
        inspectionReportDto.setInspectionDate(new Date());
        inspectionReportDto.setInspectionTime(new Date());
        inspectionReportDto.setStatus("Full Compliance");
        inspectionReportDto.setBestPractice("best practice!!!!");
        inspectionReportDto.setMarkedForAudit(true);
        inspectionReportDto.setNcRegulation(ncRegulation());
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
            insRepClient.saveData(appPremisesRecommendationDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            return false;
        }
        return true;

    }

    @Override
    public ApplicationViewDto getApplicationViewDto(String appNo) {
        ApplicationViewDto applicationViewDto = insRepClient.getAppViewByNo(appNo).getEntity();
        return applicationViewDto;
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
