package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

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
    private ApplicationGroupService applicationGroupService;
    @Autowired
    private HcsaApplicationDelegator hcsaApplicationDelegator;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;
    @Autowired
    private HcsaAppClient hcsaAppClient;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

    @Autowired
    private FillupChklistService fillupChklistService;

    //getInsRepDto and  when app status APST019 save ReportResultDto
    @Override
    public InspectionReportDto getInsRepDto(TaskDto taskDto, ApplicationViewDto applicationViewDto, LoginContext loginContext) {
        InspectionReportDto inspectionReportDto = new InspectionReportDto();
        //get Observation
        String observation = fillupChklistService.getObservationByAppPremCorrId(taskDto.getRefNo());
        StringBuilder observationSb=new StringBuilder();
        if(StringUtil.isEmpty(observation)) {
            observationSb.append('-');
        }else {
            String[] observations = observation.split("\n");
            if(!StringUtil.isEmpty(observation)){
                observationSb =new StringBuilder();
                for (String rk:observations
                ) {
                    observationSb.append(rk).append("<br>");
                }
            }
        }
        inspectionReportDto.setObservation(observationSb.toString());
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        String applicationDtoId = applicationDto.getId();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String status = applicationDto.getStatus();
        applicationDto.getApplicationType();
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
        if(!IaisCommonUtils.isEmpty(principalOfficer)){
            for (AppGrpPersonnelDto appGrpPersonnelDto : principalOfficer) {
                String name = appGrpPersonnelDto.getName();
                poNames.add(name);
            }
        }

        List<AppGrpPersonnelDto> cgos = appInsRepDto.getCgos();
        List<String> cgoNames = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(cgos)){
            for (AppGrpPersonnelDto appGrpPersonnelDto : cgos) {
                String name = appGrpPersonnelDto.getName();
                cgoNames.add(name);
            }
        }
        inspectionReportDto.setPrincipalOfficers(poNames);
        inspectionReportDto.setClinicalGovernanceOfficer(cgoNames);
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
        String reasonForVisit = getReasonForVisit(applicationType,licId,applicationViewDto.getApplicationGroupDto().getIsPreInspection());
        //serviceId transform serviceCode
        List<String> list = IaisCommonUtils.genNewArrayList();
        String serviceId = appInsRepDto.getServiceId();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcName = "";
        if (!IaisCommonUtils.isEmpty(listHcsaServices)) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcName = hcsaServiceDto.getSvcName();
            }
        }
        if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            hcsaRiskScoreDto.setLicId(licId);
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            applicationDtos.add(applicationDto);
            hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            hcsaRiskScoreDto.setServiceId(serviceId);
            hcsaRiskScoreDto.setBeExistAppId(applicationDtoId);
            HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            Double riskScore = entity.getRiskScore();
            String riskLevel = entity.getRiskLevel();
            inspectionReportDto.setRiskLevel(riskLevel);
            applicationDto.setRiskScore(riskScore);
        }
        List<String> serviceSubTypeName = getServiceSubTypeName(appPremisesCorrelationId);
        if (IaisCommonUtils.isEmpty(serviceSubTypeName)) {
            serviceSubTypeName.add("-");
        }

        Map<String,String> vehMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(applicationViewDto.getAppSvcVehicleDtos())){
            applicationViewDto.getAppSvcVehicleDtos().forEach(veh -> vehMap.put(veh.getVehicleName(),veh.getDisplayName()));
        }

        inspectionReportDto.setSubsumedServices(serviceSubTypeName);
        //Nc
        List<ReportNcRegulationDto> listReportNcRegulationDto = IaisCommonUtils.genNewArrayList();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = IaisCommonUtils.genNewArrayList();
        //add ReportNcRegulationDto and add ncItemId
        List<NcAnswerDto> ncAnswerDtoList = insepctionNcCheckListService.getNcAnswerDtoList(appPremisesCorrelationId);
        if (ncAnswerDtoList != null && !ncAnswerDtoList.isEmpty()) {
            for (NcAnswerDto ncAnswerDto : ncAnswerDtoList) {
                ReportNcRegulationDto reportNcRegulationDto = new ReportNcRegulationDto();
                reportNcRegulationDto.setNc(ncAnswerDto.getItemQuestion());
                reportNcRegulationDto.setNcs(ncAnswerDto.getNcs());
                reportNcRegulationDto.setVehicleName(vehMap.get(ncAnswerDto.getVehicleName()));
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
            if (!IaisCommonUtils.isEmpty(listAppPremisesPreInspectionNcItemDtos)) {
                for (AppPremisesPreInspectionNcItemDto preInspNc : listAppPremisesPreInspectionNcItemDtos) {
                    String adhocQuestion = preInspNc.getAdhocQuestion();
                    ReportNcRectifiedDto reportNcRectifiedDto = new ReportNcRectifiedDto();
                    if (!StringUtil.isEmpty(adhocQuestion)) {
                        reportNcRectifiedDto.setNc(adhocQuestion);
                    } else {
                        ChecklistItemDto cDto = hcsaChklClient.getChklItemById(preInspNc.getItemId()).getEntity();
                        reportNcRectifiedDto.setNc(cDto.getChecklistItem());
                    }
                    reportNcRectifiedDto.setRectified(preInspNc.getIsRecitfied() == 1 ? "Yes" : "No");
                    reportNcRectifiedDto.setNcs(preInspNc.getNcs());
                    reportNcRectifiedDto.setVehicleName(vehMap.get(preInspNc.getVehicleName()));
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
        StringBuilder bestPractice = new StringBuilder("-");
        StringBuilder remarks = new StringBuilder("-");
        if(NcRecommendationDto==null){
            inspectionReportDto.setMarkedForAudit("No");
        }else  {
            Date recomInDate = NcRecommendationDto.getRecomInDate();
            if(recomInDate==null){
                inspectionReportDto.setMarkedForAudit("No");
            }else {
                inspectionReportDto.setMarkedForAudit("Yes");
                inspectionReportDto.setTcuDate(recomInDate);
            }
            String ncBestPractice = NcRecommendationDto.getBestPractice();
            String ncRemarks = NcRecommendationDto.getRemarks();
            String[] observations=new String[]{};
            if(ncRemarks!=null){
                observations=ncRemarks.split("\n");
            }
            String[] recommendations=new String[]{};
            if(ncBestPractice!=null){
                recommendations=ncBestPractice.split("\n");
            }
            if(!StringUtil.isEmpty(ncBestPractice)){
                bestPractice = new StringBuilder();
                for (String bp:recommendations
                ) {
                    bestPractice.append(bp).append("<br>");
                }
            }
            if(!StringUtil.isEmpty(ncRemarks)){
                remarks =new StringBuilder();
                for (String rk:observations
                ) {
                    remarks.append(rk).append("<br>");
                }
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

        //noted by
        List<TaskDto> entity = organizationClient.getTasksByRefNo(appPremisesCorrelationId).getEntity();
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
        String hciCode = applicationViewDto.getHciCode();
        if (StringUtil.isEmpty(hciCode)) {
            hciCode = "-";
        }
        inspectionReportDto.setHciCode(hciCode);
        inspectionReportDto.setHciName(applicationViewDto.getHciName());
        inspectionReportDto.setHciAddress(applicationViewDto.getHciAddress());
        inspectionReportDto.setReasonForVisit(reasonForVisit);
        inspectionReportDto.setInspectionDate(inspectionDate);
        inspectionReportDto.setInspectionStartTime(inspectionStartTime);
        inspectionReportDto.setInspectionEndTime(inspectionEndTime);
        inspectionReportDto.setBestPractice(bestPractice.toString());
        inspectionReportDto.setTaskRemarks(remarks.toString());
        inspectionReportDto.setCurrentStatus(status);
        try {
            if(status.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW)){
                ReportResultDto reportResultDto = new ReportResultDto();
                reportResultDto.setInspDate(inspectionDate);
                reportResultDto.setInspEnddate(new Date());
                reportResultDto.setAppPremId(appPremisesCorrelationId);
                reportResultDto.setRiskLevel(inspectionReportDto.getRiskLevel());
                if (appPremPreInspectionNcDto != null) {
                    reportResultDto.setNc(true);
                }else {
                    reportResultDto.setNc(false);
                }
                saveReportResult(reportResultDto);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return inspectionReportDto;
    }
    private String getReasonForVisit(String applicationType,String licenceId,Integer isPre){
        String applicationTypeOldDesc = "";
        if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)){
            if(!StringUtil.isEmpty(licenceId)){
                List<LicAppCorrelationDto> licAppCorrelationDtos = hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();
                if (!IaisCommonUtils.isEmpty(licAppCorrelationDtos)) {
                    String applicationId = licAppCorrelationDtos.get(0).getApplicationId();
                    ApplicationDto applicationDtoOld = applicationClient.getApplicationById(applicationId).getEntity();
                    String applicationTypeOld = applicationDtoOld.getApplicationType();
                    applicationTypeOldDesc = MasterCodeUtil.getCodeDesc(applicationTypeOld);
                }
            }
        }
        return IaisCommonUtils.getReasonForVisitInspectionReport(applicationType,MasterCodeUtil.getCodeDesc(applicationType),isPre,applicationTypeOldDesc);
    }


    private List<String> getServiceSubTypeName(String correlationId) {
        List<String> serviceSubtypeName = IaisCommonUtils.genNewArrayList();
        List<AppSvcPremisesScopeDto> scopeList = insRepClient.getAppSvcPremisesScopeListByCorreId(correlationId).getEntity();
        for (AppSvcPremisesScopeDto appSvcPremisesScopeDto : scopeList) {
            boolean isSubService = appSvcPremisesScopeDto.isSubsumedType();
            if (isSubService) {
                String serviceId = appSvcPremisesScopeDto.getScopeName();
                HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
                if (hcsaServiceDto != null) {
                    String svcName = hcsaServiceDto.getSvcName();
                    serviceSubtypeName.add(svcName);
                }
            }
        }
        return serviceSubtypeName;
    }


    @Override
    public void saveRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        appPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        insRepClient.saveRecommendationData(appPremisesRecommendationDto);
    }

    @Override
    public void saveReportResult(ReportResultDto reportResultDto) {
        insRepClient.saveReportResult(reportResultDto).getEntity();
    }

    @Override
    public void updateengageRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        String remarks = appPremisesRecommendationDto.getRemarks();
        Integer version = 1;
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if (oldAppPremisesRecommendationDto == null && !StringUtil.isEmpty(remarks)) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
            return;
        } else if (oldAppPremisesRecommendationDto != null && !StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(remarks);
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        } else if (oldAppPremisesRecommendationDto != null && StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
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
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if (oldAppPremisesRecommendationDto == null && !StringUtil.isEmpty(remarks)) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
        } else if (oldAppPremisesRecommendationDto != null && !StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(appPremisesRecommendationDto.getRemarks());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
        } else if (oldAppPremisesRecommendationDto != null && StringUtil.isEmpty(remarks)) {
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            return;
        }
    }

    @Override
    public void updateRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        //update old data
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if (oldAppPremisesRecommendationDto != null) {
            int newVersion = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            appPremisesRecommendationDto.setVersion(newVersion);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
        } else {
            appPremisesRecommendationDto.setVersion(1);
            saveRecommendation(appPremisesRecommendationDto);
        }
        return;
    }

    @Override
    public void updateRiskLevelRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
        Integer version = 1;
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        AppPremisesRecommendationDto oldAppPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        if (oldAppPremisesRecommendationDto == null) {
            appPremisesRecommendationDto.setVersion(version);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);
        } else {
            oldAppPremisesRecommendationDto.setAuditTrailDto(currentAuditTrailDto);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
            version = oldAppPremisesRecommendationDto.getVersion() + 1;
            oldAppPremisesRecommendationDto.setVersion(version);
            oldAppPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            oldAppPremisesRecommendationDto.setRemarks(appPremisesRecommendationDto.getRemarks());
            oldAppPremisesRecommendationDto.setId(null);
            insRepClient.saveRecommendationData(oldAppPremisesRecommendationDto);
        }
        return;
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
    public String getPeriodDefault(ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        log.info("-----getPeriodDefault start -----");
        log.info(JsonUtil.parseToJson(applicationViewDto));
        log.info(JsonUtil.parseToJson(taskDto));
        String defaultOption = null;
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceDto> listHcsaServices = hcsaChklClient.getHcsaServiceByIds(list).getEntity();
        String svcCode = "";
        Double riskScore = 0d;
        if (listHcsaServices != null && !listHcsaServices.isEmpty()) {
            for (HcsaServiceDto hcsaServiceDto : listHcsaServices) {
                svcCode = hcsaServiceDto.getSvcCode();
                log.info(svcCode);
            }
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
            hcsaRiskScoreDto.setLicId(appInsRepDto.getLicenceId());
            hcsaRiskScoreDto.setAppType(applicationType);
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            applicationDtos.add(applicationViewDto.getApplicationDto());
            hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            hcsaRiskScoreDto.setServiceId(serviceId);
            hcsaRiskScoreDto.setBeExistAppId(applicationViewDto.getApplicationDto().getId());
            HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            riskScore = entity.getRiskScore();
            log.info(JsonUtil.parseToJson(entity));
        }
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        riskAcceptiionDto.setRiskScore(riskScore);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            log.info(JsonUtil.parseToJson(listRiskResultDto));
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String count = String.valueOf(riskResultDto.getTimeCount());
                Boolean dafLicture = riskResultDto.isDafLicture();
                if (dafLicture&&!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)) {
                    defaultOption = count + " " + dateType;
                }
            }
        }
        log.info(StringUtil.changeForLog("-----getPeriodDefault end-----" + "defaultOption :" + defaultOption));
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
    public void routingTaskToAo1(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, AppPremisesRecommendationDto appPremisesRecommendationDto) throws Exception {
        String serviceId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String applicationType = applicationDto.getApplicationType();
        String taskKey = taskDto.getTaskKey();
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT);
        completedTask(taskDto, applicationNo);
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId1 = hcsaSvcStageWorkingGroupDto1.getGroupId();
        List<TaskDto> taskDtos = prepareTaskToAo1(taskDto, applicationDto, hcsaSvcStageWorkingGroupDto2);
        taskService.createTasks(taskDtos);
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(taskDto.getRefNo()).getEntity();
            hcsaRiskScoreDto.setLicId(appInsRepDto.getLicenceId());
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            applicationDtos.add(applicationDto);
            hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            hcsaRiskScoreDto.setServiceId(serviceId);
            hcsaRiskScoreDto.setBeExistAppId(applicationDto.getId());
            HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            Double riskScore = entity.getRiskScore();
            applicationDto.setRiskScore(riskScore);
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT.equals(status)) {
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, appPremisesRecommendationDto.getProcessRemarks(), InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
        } else {
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, appPremisesRecommendationDto.getProcessRemarks(), InspectionConstants.INSPECTION_STATUS_PROCESSING_DECISION_REPLY, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
        }
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW);
        createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_INSPECTIOR, groupId1, subStage);
    }

    @Override
    public void routingTaskToAo2(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks, String newCorrelationId) throws Exception {
        String serviceId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        final String userId = taskDto.getUserId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String taskKey = taskDto.getTaskKey();
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
        completedTask(taskDto, applicationNo);
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId = hcsaSvcStageWorkingGroupDto1.getGroupId();
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
        applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
        updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
        createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_INSPECTION_REPORT, RoleConsts.USER_ROLE_AO1, groupId, subStage);
        List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(applicationDtoList, saveApplicationDtoList);
        removeCurrentApplicationDto(saveApplicationDtoList, applicationDto.getId());
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);

        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtos,
                HcsaConsts.ROUTING_STAGE_AO2, RoleConsts.USER_ROLE_AO2, IaisEGPHelper.getCurrentAuditTrailDto(), taskDto.getRoleId(), taskDto.getWkGrpId());
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
        if (!StringUtil.isEmpty(userId)) {
            taskDtos.forEach(t -> {
                t.setUserId(userId);
            });
        }
        createHistoryList(appPremisesRoutingHistoryDtos);
        taskService.createTasks(taskDtos);
    }

    private List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(applicationDtos)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())) {
                    continue;
                }
                if (!applicationDto.isFastTracking()) {
                    result.add(applicationDto);
                }
            }
        }
        return result;
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status) {
        if (!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (applicationId.equals(applicationDto.getId())) {
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private void createHistoryList(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        if (!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)) {
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos) {
                appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
            }
        }
    }


    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status) {
        if (IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)) {
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for (ApplicationDto applicationDto : applicationDtos) {
            if (status.equals(applicationDto.getStatus())) {
                if (applicationDtoList == null) {
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                } else {
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }

    private List<ApplicationDto> removeCurrentApplicationDto(List<ApplicationDto> applicationDtoList, String currentId) {
        List<ApplicationDto> result = null;
        if (!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(currentId)) {
            result = IaisCommonUtils.genNewArrayList();
            for (ApplicationDto applicationDto : applicationDtoList) {
                if (currentId.equals(applicationDto.getId())) {
                    continue;
                }
                result.add(applicationDto);
            }
        }
        return result;
    }

    @Override
    public void routBackTaskToInspector(TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks) {
        String serviceId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
        String status = applicationDto.getStatus();
        String applicationNo = applicationDto.getApplicationNo();
        String taskKey = taskDto.getTaskKey();
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        completedTask(taskDto, applicationNo);
        String subStage = getSubStage(appPremisesCorrelationId, taskKey);
        //ins ao1
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1 = getHcsaSvcStageWorkingGroupDto(serviceId, 2, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        //ins
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto2 = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_INS, applicationDto);
        String groupId = hcsaSvcStageWorkingGroupDto1.getGroupId();
        String userId = getRollbackInspectorId(taskDto.getRefNo());
        taskDto.setWkGrpId(hcsaSvcStageWorkingGroupDto2.getGroupId());
        List<TaskDto> taskDtos = prepareBackTaskList(taskDto, userId, applicationDto);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, InspectionConstants.PROCESS_DECI_REVISE_INSPECTION_REPORT,RoleConsts.USER_ROLE_AO1, groupId, subStage);
        createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, null, null, RoleConsts.USER_ROLE_AO1, groupId, subStage);
    }

    private String getRollbackInspectorId(String corrId) {
        String userId = taskOrganizationClient.getRollBackInspector(corrId).getEntity();
        return userId;
    }

    @Override
    public void routTaskToRoutBack(BaseProcessClass bpc, TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks) throws Exception {
        String serviceId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
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
            createTaskStatusAoRouteBackInspector(applicationDto,nextStatus,appPremisesCorrelationId,taskDto,applicationNo,stageId,userId,roleId,status,taskKey,historyRemarks,subStage);
        } else {
            String componentValue = historyExtDto.getComponentValue();
            if ("N".equals(componentValue)) {
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(serviceId,
                        stageId, applicationType, applicationGroupDto.getIsPreInspection());
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
                    log.debug(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            } else {
                createTaskStatusAoRouteBackInspector(applicationDto,nextStatus,appPremisesCorrelationId,taskDto,applicationNo,stageId,userId,roleId,status,taskKey,historyRemarks,subStage);
            }
        }
    }

    private void createTaskStatusAoRouteBackInspector(ApplicationDto applicationDto,String nextStatus,String appPremisesCorrelationId,TaskDto taskDto,
                                                      String applicationNo,String stageId,String userId,String roleId,String status,
                                                      String taskKey,String historyRemarks,String subStage){
        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, nextStatus);
        updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        String rountHistotyGroupId = taskDto.getWkGrpId();
        String rountHistotyRoleId =  taskDto.getRoleId();
        completedTask(taskDto, applicationNo);
        setWorkGroupIdForTask(applicationDto,taskDto,stageId);
        List<TaskDto> taskDtos = prepareRoutBackTaskList(taskDto, userId, roleId, stageId);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, ApplicationConsts.PROCESSING_DECISION_REPLY, rountHistotyRoleId,rountHistotyGroupId , subStage);
        createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(), taskKey, null, null,  rountHistotyRoleId, rountHistotyGroupId, subStage);
    }
    private void setWorkGroupIdForTask(ApplicationDto applicationDto, TaskDto taskDto,String stageId){
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = taskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
        hcsaSvcStageWorkingGroupDtos =taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if( !IaisCommonUtils.isEmpty(hcsaSvcStageWorkingGroupDtos) && StringUtil.isNotEmpty(hcsaSvcStageWorkingGroupDtos.get(0).getGroupId())){
            taskDto.setWkGrpId(hcsaSvcStageWorkingGroupDtos.get(0).getGroupId());
        }else if(HcsaConsts.ROUTING_STAGE_AO2.equalsIgnoreCase(stageId)){
            taskDto.setWkGrpId("A03EDD16-F90C-EA11-BE7D-000C29F371DC");
        }else if(HcsaConsts.ROUTING_STAGE_AO3.equalsIgnoreCase(stageId)){
            taskDto.setWkGrpId("4C43D448-F90C-EA11-BE7D-000C29F371DC");
        }
    }

    @Override
    public void routTaskToRoutBackAo3(BaseProcessClass bpc, TaskDto taskDto, ApplicationDto applicationDto, String appPremisesCorrelationId, String historyRemarks,boolean ao1Role) {
            String serviceId = StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId();
            String status = applicationDto.getStatus();
            String applicationNo = applicationDto.getApplicationNo();
            String taskKey = taskDto.getTaskKey();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtosByAppNo = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationNo);
            String userId = null;
            String roleId = null;
            String stageId = null;
            if (!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtosByAppNo)) {
                for (AppPremisesRoutingHistoryDto dto : appPremisesRoutingHistoryDtosByAppNo) {
                    String roleId1 = dto.getRoleId();
                    String actionby = dto.getActionby();
                    String stageId1 = dto.getStageId();
                    if (RoleConsts.USER_ROLE_AO3.equals(roleId1)) {
                        userId = actionby;
                        roleId = roleId1;
                        stageId = stageId1;
                    }
                }
            }
            String subStage = getSubStage(appPremisesCorrelationId, taskKey);
            String nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
            ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, nextStatus);
            updateInspectionStatus(appPremisesCorrelationId, InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
            String rountHistotyGroupId = taskDto.getWkGrpId();
            String rountHistotyRoleId =  taskDto.getRoleId();
            completedTask(taskDto, applicationNo);
            if(RoleConsts.USER_ROLE_AO3.equals(roleId)){
                String workGroupId = getHcsaSvcStageWorkingGroupDto(serviceId, 1, HcsaConsts.ROUTING_STAGE_AO3, applicationDto).getGroupId();
                taskDto.setWkGrpId(StringUtil.isNotEmpty(workGroupId) ? workGroupId : "4C43D448-F90C-EA11-BE7D-000C29F371DC");
            }
            List<TaskDto> taskDtos = prepareRoutBackTaskList(taskDto, userId, roleId, stageId);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationNo, status, taskKey, historyRemarks, ApplicationConsts.PROCESSING_DECISION_REPLY, rountHistotyRoleId, rountHistotyGroupId, subStage);
            if(RoleConsts.USER_ROLE_AO3.equals(roleId)){
                subStage = null;
                stageId = HcsaConsts.ROUTING_STAGE_AO3;
            }
            createAppPremisesRoutingHistory(applicationNo, updateApplicationDto.getStatus(),stageId, null, null, rountHistotyRoleId, rountHistotyGroupId, subStage);

        }

    @Override
    public InspectionReportDto getInspectorUser(TaskDto taskDto, LoginContext loginContext) {
        InspectionReportDto reportDtoForInspector = new InspectionReportDto();
        //get reported By
        List<String> listUserId = IaisCommonUtils.genNewArrayList();
        String userId = loginContext.getUserId();
        String wkGrpId = taskDto.getWkGrpId();
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
        String leadName = null;
        if (!IaisCommonUtils.isEmpty(leadId)) {
            leadName = leadList.get(0).getDisplayName();
        }
        reportDtoForInspector.setReportedBy(reportBy);
        reportDtoForInspector.setReportNoteBy(leadName);
        Set<String> inspectiors = taskService.getInspectiors(taskDto.getApplicationNo(), TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
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
                if (entity != null) {
                    String groupDomain = entity.getGroupDomain();
                    if (groupDomain.contains("hcsa")) {
                        workId = entity.getId();
                        break;
                    }
                }
            }
            List<String> leadId = organizationClient.getInspectionLead(workId).getEntity();
            for (String lead : leadId) {
                listUserId.add(lead);
            }
            List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(listUserId).getEntity();
            if (!IaisCommonUtils.isEmpty(leadList)) {
                String leadName = leadList.get(0).getDisplayName();
                reportDtoForAo.setReportNoteBy(leadName);
            } else {
                reportDtoForAo.setReportNoteBy(AppConsts.EMPTY_STR);
            }
            reportDtoForAo.setReportedBy(reportBy);

            Set<String> inspectiors = taskService.getInspectiors(applicationNo, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
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
    public void sendPostInsTaskFeData(String eventRefNum, String submissionId) {
        log.info("post inspection call back start ===================>>>>>");
        log.info(StringUtil.changeForLog("post inspection start eventRefNum groNo ===================>>>>>" + eventRefNum));
        List<ApplicationDto> postInspectionApps = applicationClient.getAppsByGrpNo(eventRefNum).getEntity();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        //appGrp --------app -------task    submissionId   operation yiyang    update licPremise
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        List<String> grpLicIds = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(postInspectionApps)) {
            for (ApplicationDto applicationDto : postInspectionApps) {
                String appGrpId = applicationDto.getAppGrpId();
                grpLicIds.add(appGrpId);
                try {
                    String corrId = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity().getId();
                    String applicationNo = applicationDto.getApplicationNo();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setServiceId(StringUtil.isEmpty(applicationDto.getBaseServiceId()) ? applicationDto.getServiceId() : applicationDto.getBaseServiceId());
                    hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    HcsaSvcStageWorkingGroupDto hsswgDto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
                    String workGroupId =hsswgDto != null ? hsswgDto.getGroupId() : "";
                    TaskDto taskDto = new TaskDto();
                    taskDto.setApplicationNo(applicationNo);
                    taskDto.setRefNo(corrId);
                    taskDto.setPriority(0);
                    taskDto.setWkGrpId(workGroupId);
                    taskDto.setAuditTrailDto(auditTrailDto);
                    taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto.setDateAssigned(new Date());
                    taskDto.setSlaDateCompleted(null);
                    taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                    taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                    taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE);
                    taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
                    taskDto.setSlaAlertInDays(0);
                    taskDto.setScore(0);
                    taskDto.setSlaInDays(0);
                    taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    taskDto.setEventRefNo(corrId);
                    //history
                    createPostRoutingHistory(applicationNo, applicationDto.getStatus(), HcsaConsts.ROUTING_STAGE_INS, null, null, RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN, null, null, auditTrailDto);
                    taskDtos.add(taskDto);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    continue;
                }
            }
        }
        log.info(StringUtil.changeForLog("================== taskDtos ===================>>>>>" + taskDtos.size()));
        log.info(StringUtil.changeForLog("==================  eventBus Start  ===================>>>>>"));
        taskService.createTasks(taskDtos);
        //eventBusHelper.submitAsyncRequest(taskDtos, submissionId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK, EventBusConsts.OPERATION_POST_INSPECTION_TASK, eventRefNum, null);
        log.info(StringUtil.changeForLog("=======================taskDtos ===================>>>>>Success"));
        log.info(StringUtil.changeForLog("==================  eventBus End  ===================>>>>>"));
        //create fe app
        log.info(StringUtil.changeForLog("==================  create fe app Start  ===================>>>>>"));
        String apps = applicationClient.fileAll(grpLicIds).getEntity();
        ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(apps, ApplicationListFileDto.class);
        List<ApplicationGroupDto> applicationGroup = applicationListFileDto.getApplicationGroup();
        if (!IaisCommonUtils.isEmpty(applicationGroup)) {
            applicationGroup.get(0).setStatus(ApplicationConsts.APPLICATION_SUCCESS_ZIP);
        }

        beEicGatewayClient.callEicWithTrack(applicationListFileDto, beEicGatewayClient::saveFePostApplicationDtos,
                "saveFePostApplicationDtos");
        log.info(StringUtil.changeForLog("==================  create fe app End  ===================>>>>>"));
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
        try {
            applicationService.updateFEApplicaiton(applicationDto);
        } catch (Exception e) {
            log.info(StringUtil.changeForLog("========================eic error===================="));
        }
        return updateApplicaiton(applicationDto);
    }

    private TaskDto completedTask(TaskDto taskDto, String appNo) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setApplicationNo(appNo);
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

    private AppPremisesRoutingHistoryDto createPostRoutingHistory(String appNo, String appStatus,
                                                                  String stageId, String internalRemarks, String processDec, String roleId, String wrkGroupId, String subStage, AuditTrailDto auditTrailDto) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        appPremisesRoutingHistoryDto.setActionby(AppConsts.USER_ID_SYSTEM);
        log.info(StringUtil.changeForLog("==================  actionBy  ===================>>>>>" + appPremisesRoutingHistoryDto.getActionby()));
        appPremisesRoutingHistoryDto.setRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN);
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
        String appNo = applicationDto.getApplicationNo();
        String userId = taskDto.getUserId();
        Set<String> ao1Report = taskService.getInspectiors(appNo, TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1, RoleConsts.USER_ROLE_AO1);
        Set<String> ao1Email = taskService.getInspectiors(appNo, TaskConsts.TASK_PROCESS_URL_INSPECTION_AO1_VALIDATE_NCEMAIL, RoleConsts.USER_ROLE_AO1);
        if (!ao1Report.isEmpty() && StringUtil.isEmpty(userId)) {
            userId = ao1Report.iterator().next();
            taskDto.setUserId(userId);
        }
        if (!ao1Email.isEmpty() && StringUtil.isEmpty(userId)) {
            userId = ao1Email.iterator().next();
            taskDto.setUserId(userId);
        }
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = taskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        String schemeType = dto.getSchemeType();
        String groupId = dto.getGroupId();
        if (StringUtil.isEmpty(userId) && TaskConsts.TASK_SCHEME_TYPE_ROUND.equals(schemeType)) {
            TaskDto taskDto1 = taskService.getUserIdForWorkGroup(groupId);
            if (taskDto1 != null) {
                userId = taskDto1.getUserId();
            }
            if (StringUtil.isEmpty(userId)) {
                List<OrgUserDto> orgUserDtos = taskOrganizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                if (!IaisCommonUtils.isEmpty(orgUserDtos)) {
                    userId = orgUserDtos.get(0).getId();
                    taskService.sendNoteToAdm(appNo, taskDto.getRefNo(), orgUserDtos.get(0));
                }
            }
            taskDto.setUserId(userId);
            taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW);
        } else if (StringUtil.isEmpty(userId) && TaskConsts.TASK_SCHEME_TYPE_COMMON.equals(schemeType)) {
            taskDto.setUserId(null);
            taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
        } else if (StringUtil.isEmpty(userId) && TaskConsts.TASK_SCHEME_TYPE_ASSIGN.equals(schemeType)) {
            taskDto.setUserId(null);
            taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION_SUPER);
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

    private List<TaskDto> prepareBackTaskList(TaskDto taskDto, String userId, ApplicationDto applicationDto) {
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = taskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
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
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
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

    private HcsaSvcStageWorkingGroupDto getHcsaSvcStageWorkingGroupDto(String serviceId, Integer order, String stageId, ApplicationDto applicationDto) {
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(stageId);
        hcsaSvcStageWorkingGroupDto.setOrder(order);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        return dto;
    }

    @Override
    public void saveAppVehs(String flag, List<AppSvcVehicleDto> appSvcVehicleDtos) {
        log.info("---------------saveAppVehs--------------------");
        if(HcsaLicenceBeConstant.EDIT_VEHICLE_FLAG.equalsIgnoreCase(flag) && IaisCommonUtils.isNotEmpty(appSvcVehicleDtos)){
            appSvcVehicleBeClient.createAppSvcVehicleDtoList(appSvcVehicleDtos);
        }
    }
}
