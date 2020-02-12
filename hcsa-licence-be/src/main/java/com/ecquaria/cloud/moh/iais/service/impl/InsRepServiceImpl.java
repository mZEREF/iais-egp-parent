package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.*;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private ComSystemAdminClient comSystemAdminClient;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;
    @Autowired
    ApplicationService applicationService;

    private final String APPROVAL="Approval";
    private final String REJECT="Reject";

    @Override
    public InspectionReportDto getInsRepDto(TaskDto taskDto, ApplicationViewDto applicationViewDto, LoginContext loginContext) {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
        //get all the inspectors by the same groupId
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String status = applicationDto.getStatus();
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(appInsRepDto.getLicenseeId()).getEntity();
        if(licenseeDto!=null){
            String name = licenseeDto.getName();
            inspectionReportDto.setLicenseeName(name);
        }
        List<AppGrpPersonnelDto> principalOfficer = appInsRepDto.getPrincipalOfficer();
        List<String> poNames = new ArrayList<>();
        for(AppGrpPersonnelDto appGrpPersonnelDto : principalOfficer){
            String name = appGrpPersonnelDto.getName();
            poNames.add(name);
        }
        inspectionReportDto.setPrincipalOfficers(poNames);


        List<String> inspectors = taskOrganizationClient.getInspectorByAppCorrId(appPremisesCorrelationId).getEntity();
        List<OrgUserDto> inspectorsNames = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> nameList = new ArrayList<>();
        for(OrgUserDto orgUserDto :inspectorsNames){
            nameList.add(orgUserDto.getDisplayName());
        }
        inspectionReportDto.setInspectOffices(nameList);


        //get application type (pre/post)
        Integer isPre = applicationGroupDto.getIsPreInspection();
        String appType = MasterCodeUtil.getCodeDesc(appTypeCode);
        String reasonForVisit;
        if (isPre == 1) {
            reasonForVisit = "Pre-licensing inspection for " + appType ;
        } else {
            reasonForVisit = "Post-licensing inspection for " + appType ;
        }

        //serviceId transform serviceCode
        List<String> list = new ArrayList<>();
        String serviceId = appInsRepDto.getServiceId();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcName = hcsaServiceDto.getSvcName();
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }

        List<HcsaSvcSubtypeOrSubsumedDto> subsumedDtos = hcsaConfigClient.listSubCorrelationFooReport(serviceId).getEntity();
        List<String> subsumedServices = new ArrayList<>();
        for(HcsaSvcSubtypeOrSubsumedDto subsumedDto :subsumedDtos){
            subsumedServices.add(subsumedDto.getName());
        }
        inspectionReportDto.setSubsumedServices(subsumedServices);

        List<ChecklistQuestionDto> listChecklistQuestionDtos = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        List<ReportNcRegulationDto> listReportNcRegulationDto = new ArrayList<>();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = new ArrayList<>();
        //add ReportNcRegulationDto and add ncItemId
        if (listChecklistQuestionDtos != null && !listChecklistQuestionDtos.isEmpty()) {
            String configId = listChecklistQuestionDtos.get(0).getConfigId();
            List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationId);
            if (ncAnswerDtoList != null && !ncAnswerDtoList.isEmpty()) {
                for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                    ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                    reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                    reportNcRegulationDto.setRegulation(ncAnswerDto.getClause());
                    listReportNcRegulationDto.add(reportNcRegulationDto);
                }
                inspectionReportDto.setStatus("Partial Compliance");
                inspectionReportDto.setNcRegulation(listReportNcRegulationDto);

            }
        }else {
            inspectionReportDto.setStatus("Full Compliance");
            inspectionReportDto.setNcRegulation(null);
        }
        //add listReportNcRectifiedDto and add ncItemId
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationId).getEntity();
        if (appPremPreInspectionNcDto != null) {
            String ncId = appPremPreInspectionNcDto.getId();
            List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
            if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                for (AppPremisesPreInspectionNcItemDto preInspNc : listAppPremisesPreInspectionNcItemDtos) {
                    ChecklistItemDto cDto = hcsaChklClient.getChklItemById(preInspNc.getItemId()).getEntity();
                    ReportNcRectifiedDto reportNcRectifiedDto = new ReportNcRectifiedDto();
                    reportNcRectifiedDto.setNc(cDto.getChecklistItem());
                    reportNcRectifiedDto.setRectified(preInspNc.getIsRecitfied() == 1 ? "Yes" : "No");
                    listReportNcRectifiedDto.add(reportNcRectifiedDto);
                }
                inspectionReportDto.setNcRectification(listReportNcRectifiedDto);
            }
        }else {
            inspectionReportDto.setNcRectification(null);
        }
        AppPremisesRecommendationDto NcRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(NcRecommendationDto==null){
            inspectionReportDto.setMarkedForAudit(false);
        }else if(NcRecommendationDto!=null&&NcRecommendationDto.getRecomInDate()!=null) {
            inspectionReportDto.setMarkedForAudit(true);
        }

        Date inspectionDate = null;
        Date inspectionStartTime = null;
        Date inspectionEndTime = null;
        AppPremisesRecommendationDto appPreRecommentdationDtoStart = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoEnd = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
        if(appPreRecommentdationDtoDate!=null){
            inspectionDate = appPreRecommentdationDtoDate.getRecomInDate();
        }
        if(appPreRecommentdationDtoStart!=null){
            inspectionStartTime = appPreRecommentdationDtoStart.getRecomInDate();
        }
        if(appPreRecommentdationDtoEnd!=null){
            inspectionEndTime = appPreRecommentdationDtoEnd.getRecomInDate();
        }
        String bestPractice = null;
        String remarks = null;
        if (NcRecommendationDto != null) {
            bestPractice = NcRecommendationDto.getBestPractice();
            remarks = NcRecommendationDto.getRemarks();
        }

        AdCheckListShowDto adhocCheckListDto = insepctionNcCheckListService.getAdhocCheckListDto(appPremisesCorrelationId);
        if(adhocCheckListDto!=null){
            inspectionReportDto.setOtherCheckList(adhocCheckListDto);
        }


        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        //todo:date
        inspectionReportDto.setInspectionDate(inspectionDate);
        inspectionReportDto.setInspectionStartTime(inspectionStartTime);
        inspectionReportDto.setInspectionEndTime(inspectionEndTime);
        inspectionReportDto.setBestPractice(bestPractice);
        inspectionReportDto.setTaskRemarks(remarks);
        inspectionReportDto.setCurrentStatus(status);
        return inspectionReportDto;
    }

    @Override
    public void saveRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        insRepClient.saveRecommendationData(appPremisesRecommendationDto);
    }

    @Override
    public void updateTcuRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_TCU_NEEDED).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
            return;
        } else {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRecomInDate(appPremisesRecommendationDto.getRecomInDate());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        }

    }

    @Override
    public void updateengageRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
            return;
        } else {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);

            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(appPremisesRecommendationDto.getRemarks());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        }

    }

    @Override
    public void updateRiskRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
            return;
        } else {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        }

    }

    @Override
    public void updateFollowRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
        } else {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);

            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(appPremisesRecommendationDto.getRemarks());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
        }

    }

    @Override
    public void updateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String recommendation = appPremisesRecommendationDto.getRecommendation();
        //update old data
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        if(oldAppPremisesRecommendationDto!=null){
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);

            if(APPROVAL.equals(recommendation)){
                oldAppPremisesRecommendationDto.setId(null);
                oldAppPremisesRecommendationDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
                oldAppPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion()+1);
                oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
                return;
            }else if(REJECT.equals(recommendation)){
                oldAppPremisesRecommendationDto.setId(null);
                oldAppPremisesRecommendationDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
                oldAppPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion()+1);
                oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
                return;
            }else {
                appPremisesRecommendationDto.setRemarks(oldAppPremisesRecommendationDto.getRemarks());
                appPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion()+1);
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(appPremisesRecommendationDto);
                return;
            }
        }else {
            appPremisesRecommendationDto.setVersion(1);
            saveRecommendation(appPremisesRecommendationDto);
            return;
        }


    }

    @Override
    public List<SelectOption> getRiskOption(ApplicationViewDto applicationViewDto) {
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = new ArrayList<>();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }
        List<SelectOption> riskResult = new ArrayList<>();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = new ArrayList<>();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count + codeDesc;
                SelectOption so = new SelectOption(recommTime, recommTime);
                riskResult.add(so);
            }
        }
        SelectOption so = new SelectOption("Others", "Others");
        riskResult.add(so);
        return riskResult;
    }

    @Override
    public List<String> getPeriods(ApplicationViewDto applicationViewDto) {
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = new ArrayList<>();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }
        List<String> riskResult = new ArrayList<>();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = new ArrayList<>();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count + codeDesc;
                riskResult.add(recommTime);
            }
        }
        return riskResult;
    }

    @Override
    public ApplicationViewDto getApplicationViewDto(String correlationId) {
        ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
        return applicationViewDto;
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }

    @Override
    public String getRobackUserId(String appId, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppIdAndStageId(appId, stageId).getEntity();
        String userId = appPremisesRoutingHistoryDto.getActionby();
        return userId;
    }

    @Override
    public void routingTaskToAo1(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String taskKey = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT);
        completedTask(taskDto);
        String subStage = getSubStage(taskDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS,applicationDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS,applicationDto);
        String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, taskKey, null, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
        List<TaskDto> taskDtos = prepareTaskToAo1(taskDto, applicationDto, hcsaSvcStageWorkingGroupDto2);
        taskService.createTasks(taskDtos);
        String groupId2 = hcsaSvcStageWorkingGroupDto2.getGroupId();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO1, groupId2, subStage);
    }

    @Override
    public void routingTaskToAo2(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String taskKey = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
        completedTask(taskDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS,applicationDto);
        String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
        String subStage = getSubStage(taskDto);
        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, taskKey, null, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_AO1, groupId1, subStage);
        List<TaskDto> taskDtos = prepareTaskToAo2(taskDto, serviceId, applicationDto);
        taskService.createTasks(taskDtos);

        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_AO2,applicationDto);
        String groupId2 = hcsaSvcStageWorkingGroupDto2.getGroupId();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO2, groupId2, null);
    }

    @Override
    public void routBackTaskToInspector(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String taskKey = taskDto.getTaskKey();
        String appId = applicationDto.getId();
        String stageId = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        completedTask(taskDto);
        String subStage = getSubStage(taskDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS,applicationDto);
        String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, taskKey, null, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
        String userId = getRobackUserId(appId, stageId);
        List<TaskDto> taskDtos = prepareBackTaskList(taskDto,userId);
        taskService.createTasks(taskDtos);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS,applicationDto);
        String groupId2 = hcsaSvcStageWorkingGroupDto2.getGroupId();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO1, groupId2, subStage);
    }

    @Override
    public InspectionReportDto getInspectorUser(TaskDto taskDto,LoginContext loginContext) {
        InspectionReportDto reportDtoForInspector = new InspectionReportDto();
        List<String> listUserId = new ArrayList<>();
        String userId = loginContext.getUserId();
        String wkGrpId = taskDto.getWkGrpId();
        String correlationId = taskDto.getRefNo();
        listUserId.add(userId);
        List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
        String reportBy = userList.get(0).getDisplayName();
        listUserId.clear();
        //get inspection lead
        List<String> leadId = organizationClient.getInspectionLead(wkGrpId).getEntity();
        for (String lead : leadId) {
            listUserId.add(lead);
        }
        List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
        String leadName = leadList.get(0).getDisplayName();
        reportDtoForInspector.setReportedBy(reportBy);
        reportDtoForInspector.setReportNoteBy(leadName);
        Set<String> inspectiors = taskService.getInspectiors(correlationId, "TSTATUS003", "INSPECTOR");
        List<String> inspectors = new ArrayList<>();
        for(String inspector :inspectiors){
            inspectors.add(inspector);
        }
        List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> inspectorsName = new ArrayList<>();
        for(OrgUserDto orgUserDto :inspectorList){
            String displayName = orgUserDto.getDisplayName();
            inspectorsName.add(displayName);
        }
        reportDtoForInspector.setInspectors(inspectorsName);
        return reportDtoForInspector;
    }

    @Override
    public InspectionReportDto getInspectorAo(TaskDto taskDto,ApplicationViewDto applicationViewDto) {
        List<String> listUserId = new ArrayList<>();
        String correlationId = taskDto.getRefNo();
        String appId = applicationViewDto.getApplicationDto().getId();
        InspectionReportDto reportDtoForAo = new InspectionReportDto();
        String userId = getRobackUserId(appId, HcsaConsts.ROUTING_STAGE_INS);
        List<String> wkGrpIds = comSystemAdminClient.getWorkGrpsByUserId(userId).getEntity();
        if(wkGrpIds!=null&&!wkGrpIds.isEmpty()){
            listUserId.add(userId);
            List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String reportBy = userList.get(0).getDisplayName();
            listUserId.clear();
            //get inspection lead
            List<String> leadId = organizationClient.getInspectionLead(wkGrpIds.get(0)).getEntity();
            for (String lead : leadId) {
                listUserId.add(lead);
            }
            List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String leadName = leadList.get(0).getDisplayName();
            reportDtoForAo.setReportedBy(reportBy);
            reportDtoForAo.setReportNoteBy(leadName);
            Set<String> inspectiors = taskService.getInspectiors(correlationId, "TSTATUS003", "INSPECTOR");
            List<String> inspectors = new ArrayList<>();
            for(String inspector :inspectiors){
                inspectors.add(inspector);
            }
            List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
            List<String> inspectorsName = new ArrayList<>();
            for(OrgUserDto orgUserDto :inspectorList){
                String displayName = orgUserDto.getDisplayName();
                inspectorsName.add(displayName);
            }
            reportDtoForAo.setInspectors(inspectorsName);
        }
        return reportDtoForAo;
    }


    private void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if(appInspectionStatusDto!=null){
            appInspectionStatusDto.setStatus(status);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
        }
    }

    private ApplicationDto updateApplicaitonStatus(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationService.updateFEApplicaiton(applicationDto);
        return updateApplicaiton(applicationDto);
    }

    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }

    private String getSubStage(TaskDto taskDto) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
        String subStage = appPremisesRoutingHistoryDto.getSubStage();
        if (subStage != null) {
            return subStage;
        } else {
            return null;
        }
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec, String roleId, String wrkGroupId, String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setWorkingGroup(wrkGroupId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }

    private List<TaskDto> prepareTaskToAo1(TaskDto taskDto, ApplicationDto applicationDto, HcsaSvcStageWorkingGroupDto dto) throws FeignException {
        List<TaskDto> list = new ArrayList<>();
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        String schemeType = dto.getSchemeType();
        String groupId = dto.getGroupId();
        if (SystemParameterConstants.ROUND_ROBIN.equals(schemeType)) {
            TaskDto taskDto1 = taskService.getUserIdForWorkGroup(groupId);
            taskDto.setUserId(taskDto1.getUserId());
        } else {
            taskDto.setUserId(null);
        }
        taskDto.setId(null);
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        taskDto.setWkGrpId(groupId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_AO1);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }

    private List<TaskDto> prepareTaskToAo2(TaskDto taskDto, String serviceId, ApplicationDto applicationDto) throws FeignException {
        List<TaskDto> list = new ArrayList<>();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_AO2);
        hcsaSvcStageWorkingGroupDto.setOrder(1);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        String schemeType = dto.getSchemeType();
        String groupId = dto.getGroupId();
        if (SystemParameterConstants.ROUND_ROBIN.equals(schemeType)) {
            TaskDto taskDto1 = taskService.getUserIdForWorkGroup(groupId);
            taskDto.setUserId(taskDto1.getUserId());
        } else {
            taskDto.setUserId(null);
        }
        taskDto.setId(null);
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_AO2);
        taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW);
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        taskDto.setWkGrpId(groupId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_AO2);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }

    private List<TaskDto> prepareBackTaskList(TaskDto taskDto,String userId) {
        List<TaskDto> list = new ArrayList<>();
        taskDto.setId(null);
        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        //taskDto.setScore(1);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId) {
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for (ApplicationDto applicationDto : applicationDtos) {
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private HcsaSvcStageWorkingGroupDto getHcsaSvcStageWorkingGroupDto(String serviceId, Integer order,String stageId, ApplicationDto applicationDto) {
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(stageId);
        hcsaSvcStageWorkingGroupDto.setOrder(order);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        return dto;
    }


}
