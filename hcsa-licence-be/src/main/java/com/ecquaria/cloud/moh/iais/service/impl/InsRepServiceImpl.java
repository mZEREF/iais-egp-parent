package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
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
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private ComSystemAdminClient comSystemAdminClient;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private ApplicationGroupService applicationGroupService;
    @Autowired
    private HcsaApplicationDelegator hcsaApplicationDelegator;


    private final String APPROVAL = "Approval";
    private final String REJECT = "Reject";

    @Override
    public InspectionReportDto getInsRepDto(TaskDto taskDto, ApplicationViewDto applicationViewDto, LoginContext loginContext) throws FeignException {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String status = applicationDto.getStatus();
        String appTypeCode = insRepClient.getAppType(appId).getEntity();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(appInsRepDto.getLicenseeId()).getEntity();
        String licId = appInsRepDto.getLicenceId();
        if (StringUtil.isEmpty(licId)) {
            inspectionReportDto.setLicenceNo("-");
        } else {
            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
            if (licenceDto != null) {
                String licenceNo = licenceDto.getLicenceNo();
                inspectionReportDto.setLicenceNo(licenceNo);
            }
        }
        if (licenseeDto != null) {
            String name = licenseeDto.getName();
            inspectionReportDto.setLicenseeName(name);
        }
        List<AppGrpPersonnelDto> principalOfficer = appInsRepDto.getPrincipalOfficer();
        List<String> poNames = IaisCommonUtils.genNewArrayList();
        for (AppGrpPersonnelDto appGrpPersonnelDto : principalOfficer) {
            String name = appGrpPersonnelDto.getName();
            poNames.add(name);
        }
        inspectionReportDto.setPrincipalOfficers(poNames);


        List<String> nameList = IaisCommonUtils.genNewArrayList();
        AppPremisesRecommendationDto otherOfficesDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
        if (otherOfficesDto != null) {
            String otherOffices = otherOfficesDto.getRemarks();
            nameList.add(otherOffices);
            inspectionReportDto.setInspectOffices(nameList);
        } else {
            String s = "-";
            nameList.add(s);
            inspectionReportDto.setInspectOffices(nameList);
        }
        //get application type (pre/post)
        Integer isPre = applicationGroupDto.getIsPreInspection();
        String appType = MasterCodeUtil.getCodeDesc(appTypeCode);
        String reasonForVisit;
        if (isPre == 1) {
            reasonForVisit = "Pre-licensing inspection for " + appType;
        } else {
            reasonForVisit = "Post-licensing inspection for " + appType;
        }

        //serviceId transform serviceCode
        List<String> list = IaisCommonUtils.genNewArrayList();
        String serviceId = appInsRepDto.getServiceId();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcName = hcsaServiceDto.getSvcName();
            }
        }

        List<HcsaSvcSubtypeOrSubsumedDto> subsumedDtos = hcsaConfigClient.listSubCorrelationFooReport(serviceId).getEntity();
        List<String> subsumedServices = IaisCommonUtils.genNewArrayList();
        if (subsumedDtos != null && !subsumedDtos.isEmpty()) {
            for (HcsaSvcSubtypeOrSubsumedDto subsumedDto : subsumedDtos) {
                subsumedServices.add(subsumedDto.getName());
            }
        } else {
            subsumedServices.add("-");
        }
        inspectionReportDto.setSubsumedServices(subsumedServices);
        //Nc
//        List<ChecklistQuestionDto> listChecklistQuestionDtos = hcsaChklClient.getcheckListQuestionDtoList(svcCode, "Inspection").getEntity();
        List<ReportNcRegulationDto> listReportNcRegulationDto = IaisCommonUtils.genNewArrayList();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = IaisCommonUtils.genNewArrayList();
        //add ReportNcRegulationDto and add ncItemId
//        if (listChecklistQuestionDtos != null && !listChecklistQuestionDtos.isEmpty()) {
        List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationId);
        if (ncAnswerDtoList != null && !ncAnswerDtoList.isEmpty()) {
            for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                String clause = ncAnswerDto.getClause();
                if (StringUtil.isEmpty(clause)) {
                    reportNcRegulationDto.setRegulation("-");
                } else {
                    reportNcRegulationDto.setRegulation(clause);
                }
                listReportNcRegulationDto.add(reportNcRegulationDto);
            }
            inspectionReportDto.setNcRegulation(listReportNcRegulationDto);
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
                inspectionReportDto.setStatus("Partial Compliance");
            }
        } else {
            inspectionReportDto.setNcRectification(null);
            inspectionReportDto.setStatus("Full Compliance");
        }
        AppPremisesRecommendationDto NcRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_TCU).getEntity();
        //best practice remarks
        String bestPractice = "-";
        String remarks = "-";
        if (NcRecommendationDto == null) {
            inspectionReportDto.setMarkedForAudit("No");
        } else if (NcRecommendationDto != null) {
            Date recomInDate = NcRecommendationDto.getRecomInDate();
            if (recomInDate == null) {
                inspectionReportDto.setMarkedForAudit("No");
            } else {
                inspectionReportDto.setMarkedForAudit("Yes");
                inspectionReportDto.setTcuDate(recomInDate);
            }
            String ncBestPractice = NcRecommendationDto.getBestPractice();
            String ncRemarks = NcRecommendationDto.getRemarks();
            if (!StringUtil.isEmpty(ncBestPractice)) {
                bestPractice = ncBestPractice;
            }
            if (!StringUtil.isEmpty(ncRemarks)) {
                remarks = ncRemarks;
            }
        }
        inspectionReportDto.setRectifiedWithinKPI("Yes");
        //Date time
        Date inspectionDate = null;
        String inspectionStartTime = null;
        String inspectionEndTime = null;
        AppPremisesRecommendationDto appPreRecommentdationDtoStart = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        AppPremisesRecommendationDto appPreRecommentdationDtoEnd = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
        if (appPreRecommentdationDtoDate != null) {
            inspectionDate = appPreRecommentdationDtoDate.getRecomInDate();
        }
        if (appPreRecommentdationDtoStart != null) {
            inspectionStartTime = appPreRecommentdationDtoStart.getRecomDecision();
        }
        if (appPreRecommentdationDtoEnd != null) {
            inspectionEndTime = appPreRecommentdationDtoEnd.getRecomDecision();
        }
        AppPremisesRecommendationDto rectiDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if (rectiDto != null) {
            String inspectypeRemarks = rectiDto.getRemarks();
            inspectionReportDto.setInspectypeRemarks(inspectypeRemarks);
        } else {
            inspectionReportDto.setInspectypeRemarks("-");
        }

        //noted  by
        List<TaskDto> entity = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        for (TaskDto dto : entity) {
            String roleId = dto.getRoleId();
            String userId = dto.getUserId();
            if (RoleConsts.USER_ROLE_AO1.equals(roleId)) {
                List<String> listAoUserId = IaisCommonUtils.genNewArrayList();
                listAoUserId.add(userId);
                List<OrgUserDto> userList1 = organizationClient.retrieveOrgUserAccount(listAoUserId).getEntity();
                String reportedNotedBy = userList1.get(0).getDisplayName();
                inspectionReportDto.setReportNoteBy(reportedNotedBy);
                break;
            } else {
                inspectionReportDto.setReportNoteBy("-");
            }
        }
        for (TaskDto dto : entity) {
            String roleId = dto.getRoleId();
            String processUrl = dto.getProcessUrl();
            String userId = dto.getUserId();
            if (RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId) && TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT.equals(processUrl)) {
                List<String> listAoUserId = IaisCommonUtils.genNewArrayList();
                listAoUserId.add(userId);
                List<OrgUserDto> userList1 = organizationClient.retrieveOrgUserAccount(listAoUserId).getEntity();
                String reportedNotedBy = userList1.get(0).getDisplayName();
                inspectionReportDto.setReportedBy(reportedNotedBy);
            }
        }
        if (StringUtil.isEmpty(inspectionReportDto.getReportedBy())) {
            List<String> listUserId = IaisCommonUtils.genNewArrayList();
            String userId = loginContext.getUserId();
            listUserId.add(userId);
            List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String reportBy = userList.get(0).getDisplayName();
            inspectionReportDto.setReportedBy(reportBy);
        }

        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciCode(applicationViewDto.getHciCode());
        inspectionReportDto.setHciName(applicationViewDto.getHciName());
        inspectionReportDto.setHciAddress(applicationViewDto.getHciAddress());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
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
    public void updateengageRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        String remarks = appPremisesRecommendationDto.getRemarks();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        if (oldAppPremisesRecommendationDto == null && !StringUtil.isEmpty(remarks)) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
            return;
        } else if (oldAppPremisesRecommendationDto != null && !StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(remarks);
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        } else if (oldAppPremisesRecommendationDto != null && StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
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
        String remarks = appPremisesRecommendationDto.getRemarks();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
        } else if (oldAppPremisesRecommendationDto != null && !StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(appPremisesRecommendationDto.getRemarks());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
        } else if (oldAppPremisesRecommendationDto != null && StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        }
    }

    @Override
    public void updateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String recommendation = appPremisesRecommendationDto.getRecommendation();
        //update old data
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        if (oldAppPremisesRecommendationDto != null) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            if (APPROVAL.equals(recommendation)) {
                oldAppPremisesRecommendationDto.setId(null);
                oldAppPremisesRecommendationDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
                oldAppPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion() + 1);
                oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
                return;
            } else if (REJECT.equals(recommendation)) {
                oldAppPremisesRecommendationDto.setId(null);
                oldAppPremisesRecommendationDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
                oldAppPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion() + 1);
                oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
                return;
            } else {
                appPremisesRecommendationDto.setRemarks(oldAppPremisesRecommendationDto.getRemarks());
                appPremisesRecommendationDto.setVersion(oldAppPremisesRecommendationDto.getVersion() + 1);
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                insRepClient.saveRecommendationData(appPremisesRecommendationDto);
                return;
            }
        } else {
            appPremisesRecommendationDto.setVersion(1);
            saveRecommendation(appPremisesRecommendationDto);
            return;
        }


    }

    @Override
    public List<SelectOption> getRiskOption(ApplicationViewDto applicationViewDto) {
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }
        List<SelectOption> riskResult = IaisCommonUtils.genNewArrayList();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                //String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                //String recommTime = count + " " + codeDesc;
                String lictureText = riskResultDto.getLictureText();
                SelectOption so = new SelectOption(count + " " + dateType, lictureText);
                riskResult.add(so);
            }
        }
        SelectOption so = new SelectOption("Others", "Others");
        riskResult.add(so);
        return riskResult;
    }

    @Override
    public String getPeriodDefault(ApplicationViewDto applicationViewDto) {
        String defaultOption = null;
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String count = String.valueOf(riskResultDto.getTimeCount());
                Boolean dafLicture = riskResultDto.isDafLicture();
                if(dafLicture){
                    defaultOption =   count + " " + dateType ;
                }
            }
        }
        return defaultOption;
    }

    @Override
    public List<String> getPeriods(ApplicationViewDto applicationViewDto) {
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
            }
        }
        List<String> riskResult = IaisCommonUtils.genNewArrayList();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count + " " + dateType;
                riskResult.add(recommTime);
            }
        }
        return riskResult;
    }

    @Override
    public ApplicationViewDto getApplicationViewDto(String correlationId) {
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(correlationId);

        return applicationViewDto;
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }

    @Override
    public String getRobackUserId(String appNo, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo, stageId).getEntity();
        String userId = appPremisesRoutingHistoryDto.getActionby();
        return userId;
    }

    @Override
    public void routingTaskToAo1(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, AppPremisesRecommendationDto appPremisesRecommendationDto) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String taskKey = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT);
        completedTask(taskDto);
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
        List<TaskDto> taskDtos = prepareTaskToAo1(taskDto, applicationDto, hcsaSvcStageWorkingGroupDto2);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(applicationNo, status, taskKey, appPremisesRecommendationDto.getProcessRemarks(), InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
        createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO1, groupId1, subStage);
    }

    @Override
    public void routingTaskToAo2(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String taskKey = taskDto.getTaskKey();
        String applicationType = applicationDto.getApplicationType();
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
        completedTask(taskDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId = hcsaSvcStageWorkingGroupDto1.getGroupId();
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) ||ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)||ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)){
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            if( !ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
                AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
                hcsaRiskScoreDto.setLicId(appInsRepDto.getLicenceId());
            }else {
                List<ApplicationDto> applicationDtos = new ArrayList<>(1);
                applicationDtos.add(applicationDto);
                hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            }
            hcsaRiskScoreDto.setServiceCode(serviceId);
            HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            Double riskScore = entity.getRiskScore();
        }
        if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) || ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK.equals(applicationType)) {
            updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_APPEAL_APPROVE);
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList, applicationDto.getApplicationNo(),
                    ApplicationConsts.APPLICATION_STATUS_APPROVED);
            if (isAllSubmit) {
                //update application Group status
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                applicationGroupService.updateApplicationGroup(applicationGroupDto);
            }
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, null, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_AO1, groupId, subStage);
        } else {
            updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
            List<TaskDto> taskDtos = prepareTaskToAo2(taskDto, serviceId, applicationDto);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_AO1, groupId, subStage);
            createAppPremisesRoutingHistory(applicationNo, applicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO2, groupId, null);
        }

    }

    @Override
    public void routBackTaskToInspector(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks) throws FeignException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String taskKey = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        completedTask(taskDto);
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId = hcsaSvcStageWorkingGroupDto1.getGroupId();
        String userId = getRobackUserId(applicationNo, taskKey);
        List<TaskDto> taskDtos = prepareBackTaskList(taskDto, userId);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId, subStage);
        createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO1, groupId, subStage);
    }

    @Override
    public void routTaskToRoutBack(BaseProcessClass bpc, TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks) throws TemplateException, FeignException, CloneNotSupportedException, IOException {
        String serviceId = applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String applicationType = applicationDto.getApplicationType();
        String taskKey = taskDto.getTaskKey();

        AppPremisesRoutingHistoryDto secondRouteBackHistoryByAppNo = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(applicationNo, status);
        String userId = secondRouteBackHistoryByAppNo.getActionby();
        String roleId = secondRouteBackHistoryByAppNo.getRoleId();
        String stageId = secondRouteBackHistoryByAppNo.getStageId();
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);

        String nextStatus = ApplicationConsts.APPLICATION_STATUS_REPLY;
        if (HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
        } else if (HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
        } else if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
            if (RoleConsts.USER_ROLE_AO1.equals(roleId)) {
                nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION;
            } else {
                nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW;
            }
        } else if (HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
        } else if (HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
        } else if (HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }

        String routeHistoryId = secondRouteBackHistoryByAppNo.getId();
        AppPremisesRoutingHistoryExtDto historyExtDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistoryExtByHistoryAndComponentName(routeHistoryId, ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW).getEntity();
        if (historyExtDto == null) {
            ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, nextStatus);
            updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
            completedTask(taskDto);

            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
            String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
            List<TaskDto> taskDtos = prepareRoutBackTaskList(taskDto, userId, roleId, stageId);
            taskService.createTasks(taskDtos);
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
            String groupId2 = hcsaSvcStageWorkingGroupDto2.getGroupId();
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
            createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, historyRemarks, null, roleId, groupId2, subStage);
        } else {
            String componentValue = historyExtDto.getComponentValue();
            if ("N".equals(componentValue)) {
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(serviceId,
                        stageId, applicationType);
                if (hcsaSvcRoutingStageDtoList != null) {
                    HcsaSvcRoutingStageDto nextStage = hcsaSvcRoutingStageDtoList.get(0);
                    String stageCode = nextStage.getStageCode();
                    String routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    String nextStageId = HcsaConsts.ROUTING_STAGE_AO2;
                    if (RoleConsts.USER_ROLE_AO3.equals(stageCode)) {
                        nextStageId = HcsaConsts.ROUTING_STAGE_AO3;
                        routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                    }
                    hcsaApplicationDelegator.routingTask(bpc, nextStageId, routeNextStatus, stageCode);
                } else {
                    log.error(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            } else {
                ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, nextStatus);
                updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
                completedTask(taskDto);

                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
                String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
                List<TaskDto> taskDtos = prepareRoutBackTaskList(taskDto, userId, roleId, stageId);
                taskService.createTasks(taskDtos);
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
                String groupId2 = hcsaSvcStageWorkingGroupDto2.getGroupId();
                createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
                createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, historyRemarks, null, roleId, groupId2, subStage);
            }
        }
    }

    @Override
    public InspectionReportDto getInspectorUser(TaskDto taskDto, LoginContext loginContext) {
        InspectionReportDto reportDtoForInspector = new InspectionReportDto();
        //get reported By
        List<String> listUserId = IaisCommonUtils.genNewArrayList();
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
        Set<String> inspectiors = taskService.getInspectiors(correlationId, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
        List<String> inspectors = IaisCommonUtils.genNewArrayList();
        for (String inspector : inspectiors) {
            inspectors.add(inspector);
        }
        List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> inspectorsName = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto orgUserDto : inspectorList) {
            String displayName = orgUserDto.getDisplayName();
            inspectorsName.add(displayName);
        }
        reportDtoForInspector.setInspectors(inspectorsName);
        return reportDtoForInspector;
    }

    @Override
    public InspectionReportDto getInspectorAo(TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        List<String> listUserId = IaisCommonUtils.genNewArrayList();
        String correlationId = taskDto.getRefNo();
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        InspectionReportDto reportDtoForAo = new InspectionReportDto();
        String userId = getRobackUserId(applicationNo, HcsaConsts.ROUTING_STAGE_INS);
        List<String> wkGrpIds = comSystemAdminClient.getWorkGrpsByUserId(userId).getEntity();
        if (wkGrpIds != null && !wkGrpIds.isEmpty()) {
            listUserId.add(userId);
            List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String reportBy = userList.get(0).getDisplayName();
            listUserId.clear();
            //get inspection lead
            String workId = null;
            for (String id : wkGrpIds) {
                WorkingGroupDto entity = organizationClient.getWrkGrpById(id).getEntity();
                String groupDomain = entity.getGroupDomain();
                if ("hcsa".equals(groupDomain)) {
                    workId = entity.getId();
                    break;
                }
            }
            List<String> leadId = organizationClient.getInspectionLead(workId).getEntity();
            for (String lead : leadId) {
                listUserId.add(lead);
            }
            List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String leadName = leadList.get(0).getDisplayName();
            reportDtoForAo.setReportedBy(reportBy);
            reportDtoForAo.setReportNoteBy(leadName);
            Set<String> inspectiors = taskService.getInspectiors(correlationId, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
            List<String> inspectors = IaisCommonUtils.genNewArrayList();
            for (String inspector : inspectiors) {
                inspectors.add(inspector);
            }
            List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
            List<String> inspectorsName = IaisCommonUtils.genNewArrayList();
            for (OrgUserDto orgUserDto : inspectorList) {
                String displayName = orgUserDto.getDisplayName();
                inspectorsName.add(displayName);
            }
            reportDtoForAo.setInspectors(inspectorsName);
        }
        return reportDtoForAo;
    }

    @Override
    public InspectionReportDto getInspectorAo2(TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        List<String> listUserId = IaisCommonUtils.genNewArrayList();
        String correlationId = taskDto.getRefNo();
        InspectionReportDto reportDtoForAo = new InspectionReportDto();
        Set<String> inspectorReportS = taskService.getInspectiors(correlationId, TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR);
        List<String> inspectorReportList = IaisCommonUtils.genNewArrayList();
        for (String inspector : inspectorReportS) {
            inspectorReportList.add(inspector);
        }
        List<OrgUserDto> reported = organizationClient.retrieveOrgUserAccount(inspectorReportList).getEntity();
        List<String> inspectorsName = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto orgUserDto : reported) {
            String displayName = orgUserDto.getDisplayName();
            inspectorsName.add(displayName);
        }
        reportDtoForAo.setReportedBy(inspectorsName.get(0));

        String userId = reported.get(0).getId();
        List<String> wkGrpIds = comSystemAdminClient.getWorkGrpsByUserId(userId).getEntity();
        if (wkGrpIds != null && !wkGrpIds.isEmpty()) {
            listUserId.add(userId);
            List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String reportBy = userList.get(0).getDisplayName();
            listUserId.clear();
            //get inspection lead
            String workId = null;
            for (String id : wkGrpIds) {
                WorkingGroupDto entity = organizationClient.getWrkGrpById(id).getEntity();
                String groupDomain = entity.getGroupDomain();
                if ("hcsa".equals(groupDomain)) {
                    workId = entity.getId();
                    break;
                }
            }
            List<String> leadId = organizationClient.getInspectionLead(workId).getEntity();
            for (String lead : leadId) {
                listUserId.add(lead);
            }
            List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            String leadName = leadList.get(0).getDisplayName();
            reportDtoForAo.setReportNoteBy(leadName);
            Set<String> inspectiors = taskService.getInspectiors(correlationId, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
            List<String> inspectors = IaisCommonUtils.genNewArrayList();
            for (String inspector : inspectiors) {
                inspectors.add(inspector);
            }
            List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
            List<String> inspectorsNames = IaisCommonUtils.genNewArrayList();
            for (OrgUserDto orgUserDto : inspectorList) {
                String displayName = orgUserDto.getDisplayName();
                inspectorsNames.add(displayName);
            }
            reportDtoForAo.setInspectors(inspectorsNames);
        }
        return reportDtoForAo;
    }

    @Override
    public void sendPostInsTaskFeData(String submissionId, String eventRefNum) throws FeignException {
        log.info("call back ===================>>>>>");
        List<ApplicationDto> postApps = applicationClient.getAppsByGrpNo(eventRefNum).getEntity();
        //appGrp --------app -------task    submissionId   operation yiyang    update licPremise
        List<TaskDto> taskDtos = new ArrayList<>();
        List<String> appGrpIds = new ArrayList<>();
        if (!postApps.isEmpty()) {
            for (ApplicationDto applicationDto : postApps) {
                String licenceId = applicationDto.getOriginLicenceId();
                List<String> licIds = IaisCommonUtils.genNewArrayList();
                licIds.clear();
                licIds.add(licenceId);
                List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtosByIds(licIds).getEntity();
                String applicationType = applicationDtos.get(0).getApplicationType();
                applicationDto.setApplicationType(applicationType);
                String corrId = applicationClient.getCorrIdByAppId(applicationDto.getId()).getEntity();
                TaskDto taskDto = taskService.getRoutingTask(applicationDto, HcsaConsts.ROUTING_STAGE_INS, RoleConsts.USER_ROLE_INSPECTIOR, corrId);
                taskDtos.add(taskDto);
                String appGrpId = applicationDto.getAppGrpId();
                appGrpIds.add(appGrpId);
            }
        }
        try {
            eventBusHelper.submitAsyncRequest(taskDtos, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_POST_INSPECTION_TASK, eventRefNum, null);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        log.info(StringUtil.changeForLog("taskDtos ===================>>>>>" + taskDtos.size()));
        taskService.createTasks(taskDtos);
        log.info(StringUtil.changeForLog("taskDtos ===================>>>>>Success" + taskDtos.size()));
        String data = applicationClient.getBeData(appGrpIds).getEntity();
        ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(data, ApplicationListFileDto.class);
        log.info(StringUtil.changeForLog("applicationGroupId ===================>>>>>Success" + applicationListDto.getApplicationGroup().get(0).getId()));


        //eic save fe data
        //applicationClient.saveFeData(applicationListDto);
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistorySubStage(String corrId, String stageId) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(corrId, stageId).getEntity();
    }

    private void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if (appInspectionStatusDto != null) {
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

    private String getSubStage(String corrId, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(corrId, stageId).getEntity();
        String subStage = appPremisesRoutingHistoryDto.getSubStage();
        if (subStage != null) {
            return subStage;
        } else {
            return null;
        }
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec, String roleId, String wrkGroupId, String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setWorkingGroup(wrkGroupId);
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGroupId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }

    private List<TaskDto> prepareTaskToAo1(TaskDto taskDto, ApplicationDto applicationDto, HcsaSvcStageWorkingGroupDto dto) throws FeignException {
        String refNo = taskDto.getRefNo();
        String userId = null;
        Set<String> inspectors = taskService.getInspectiors(refNo, TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1, RoleConsts.USER_ROLE_AO1);
        if (!inspectors.isEmpty()) {
            userId = inspectors.iterator().next();
            taskDto.setUserId(userId);
        }
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        String schemeType = dto.getSchemeType();
        String groupId = dto.getGroupId();
        if (StringUtil.isEmpty(userId) && SystemParameterConstants.ROUND_ROBIN.equals(schemeType)) {
            TaskDto taskDto1 = taskService.getUserIdForWorkGroup(groupId);
            taskDto.setUserId(taskDto1.getUserId());
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
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        list.add(taskDto);
        return list;
    }

    private List<TaskDto> prepareTaskToAo2(TaskDto taskDto, String serviceId, ApplicationDto applicationDto) throws FeignException {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_AO2);
        hcsaSvcStageWorkingGroupDto.setOrder(1);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
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

    private List<TaskDto> prepareBackTaskList(TaskDto taskDto, String userId) {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        taskDto.setId(null);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }

    private List<TaskDto> prepareRoutBackTaskList(TaskDto taskDto, String userId, String roleId, String stageId) {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        taskDto.setId(null);
        taskDto.setTaskKey(stageId);
        taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(roleId);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId) {
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for (ApplicationDto applicationDto : applicationDtos) {
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private HcsaSvcStageWorkingGroupDto getHcsaSvcStageWorkingGroupDto(String serviceId, Integer order, String stageId, ApplicationDto applicationDto) {
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(stageId);
        hcsaSvcStageWorkingGroupDto.setOrder(order);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        return dto;
    }

    private String getTaskType(String serviceId, Integer order, String stageId, String appType) {
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(stageId);
        hcsaSvcStageWorkingGroupDto.setOrder(order);
        hcsaSvcStageWorkingGroupDto.setType(appType);
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        String schemeType = dto.getSchemeType();
        return schemeType;
    }


}
