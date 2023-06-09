package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.method.RfiCanCheck;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationViewServiceImp implements ApplicationViewService {

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    CessationClient cessationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    LicenceService licenceService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private RfiCanCheck rfiCanCheck;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ConfigCommClient configCommClient;

    @Override
    public ApplicationViewDto searchByCorrelationIdo(String correlationId) {
        //return applicationClient.getAppViewByNo(appNo).getEntity();
        return applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
    }

    @Override
    public ApplicationDto getApplicaitonByAppNo(String appNo) {
        return applicationClient.getAppByNo(appNo).getEntity();
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {

        return  applicationClient.updateApplication(applicationDto).getEntity();
    }

    @Override
    public List<OrgUserDto> getUserNameById(List<String> userIdList) {

        return  organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList) {

        return  configCommClient.listSvcDocConfigByIds(titleIdList).getEntity();
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStage(String serviceId, String stageId) {
        return hcsaConfigClient.getStageName(serviceId, stageId).getEntity();
    }

    @Override
    public List<HcsaSvcRoutingStageDto> getStage(String serviceId, String stageId, String type, Integer isPreIns) {
        return hcsaConfigClient.getStageName(serviceId, stageId, type, isPreIns).getEntity();
    }

    @Override
    public OrgUserDto getUserById(String userId) {
        return organizationClient.retrieveOrgUserAccountById(userId).getEntity();
    }

    @Override
    public String getWrkGrpName(String id) {
        String result = null;
        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(id).getEntity();
        if(workingGroupDto != null){
            if(AppConsts.DOMAIN_TEMPORARY.equals(workingGroupDto.getGroupDomain())){
                result = AppConsts.WORK_GROUP_BROADCAST;
            }else{
                result = workingGroupDto.getGroupName();
            }
        }
        return result;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoById(String id) {
        return HcsaServiceCacheHelper.getServiceById(id);
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> getHcsaSvcSubtypeOrSubsumedByServiceId(String serviceId) {
        return hcsaConfigClient.listSubCorrelation(serviceId).getEntity();
    }

    @Override
    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id) {
        return applicationClient.getLastAppPremisesCorrelationDtoByCorreId(id).getEntity();
    }

    @Override
    public HcsaSvcRoutingStageDto getStageById(String id) {
        return hcsaConfigClient.getHcsaSvcRoutingStageById(id).getEntity();
    }

    @Override
    public ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId) {
        return getApplicationViewDtoByCorrId(appCorId,null);
    }

    @Override
    public ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId,String currentRoleId) {
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(appCorId);
        ApplicationViewDto applicationViewDto = applicationViewService.searchByCorrelationIdo(appCorId);
        if (IaisCommonUtils.isNotEmpty(applicationViewDto.getAppPremSpecialSubSvcRelDtoList())) {
            for (AppPremSubSvcRelDto apst : applicationViewDto.getAppPremSpecialSubSvcRelDtoList()) {
                apst.setSvcName(HcsaServiceCacheHelper.getServiceNameById(apst.getSvcId()));
            }
        }
        if (IaisCommonUtils.isNotEmpty(applicationViewDto.getAppPremOthersSubSvcRelDtoList())) {
            for (AppPremSubSvcRelDto apst : applicationViewDto.getAppPremOthersSubSvcRelDtoList()) {
                apst.setSvcName(HcsaServiceCacheHelper.getServiceNameById(apst.getSvcId()));
            }
        }
        initDocuments(applicationViewDto);
        String applicationType= MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationType());
        applicationViewDto.setApplicationType(applicationType);
        String status = MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getStatus());
        applicationViewDto.setCurrentStatus(status);
//        if(!StringUtil.isEmpty(applicationViewDto.getSubmissionDate()))
//        applicationViewDto.setSubmissionDate(IaisEGPHelper.parseToString(IaisEGPHelper.parseToDate( applicationViewDto.getSubmissionDate(),"yyyy-MM-dd hh:mm"),"yyyy-MM-dd"));
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        applicationViewDto.setServiceType(hcsaServiceDto.getSvcName());
        applicationViewDto.setSvcCode(hcsaServiceDto.getSvcCode());
        List<String> actionByList= IaisCommonUtils.genNewArrayList();
        for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:applicationViewDto.getAppPremisesRoutingHistoryDtoList()
        ) {
            actionByList.add(appPremisesRoutingHistoryDto.getActionby());

        }
        List<OrgUserDto> actionByRealNameList=applicationViewService.getUserNameById(actionByList);
        for (int i = 0; i <applicationViewDto.getAppPremisesRoutingHistoryDtoList().size(); i++) {
            String username="-";
            for (OrgUserDto orgUserDto : actionByRealNameList) {
                if ((applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getActionby()).equals(orgUserDto.getId())) {
                    username = orgUserDto.getDisplayName();
                    break;
                }
            }
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setActionby(username);
            String statusUpdate=MasterCodeUtil.getCodeDesc(applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getAppStatus());
            applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setAppStatus(statusUpdate);
            String workGroupId = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getWrkGrpId();
            String processDecision = applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).getProcessDecision();
            if(StringUtil.isEmpty(processDecision)){
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(statusUpdate);
            }else{
                String codeDesc = StringUtil.isEmpty(MasterCodeUtil.getCodeDesc(processDecision)) ? processDecision : MasterCodeUtil.getCodeDesc(processDecision);
                applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setProcessDecision(codeDesc);
            }


            if (!StringUtil.isEmpty(workGroupId)){
                log.info(StringUtil.changeForLog("Wrk Group Id ======>" + workGroupId));
                String workingGroupName=applicationViewService.getWrkGrpName(workGroupId);
                if (!StringUtil.isEmpty(workingGroupName)){
                    applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setWorkingGroup(workingGroupName);
                }else{
                    applicationViewDto.getAppPremisesRoutingHistoryDtoList().get(i).setWorkingGroup("-");
                }
            }
        }
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);

        //set internal files
        List<AppIntranetDocDto> intranetDocDtos =  fillUpCheckListGetAppClient.getAppIntranetDocListByPremIdAndStatus(appCorId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        //applicationViewService
        for(AppIntranetDocDto intranetDocDto : intranetDocDtos){
            OrgUserDto user = applicationViewService.getUserById(intranetDocDto.getSubmitBy());
            if(user!=null){
                intranetDocDto.setSubmitByName(user.getDisplayName());
            }
            intranetDocDto.setSubmitDtString(Formatter.formatDateTime(intranetDocDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss"));
        }
        applicationViewDto.setAppIntranetDocDtoList(intranetDocDtos);

        //get AppPremisesRecommendationDto
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorId,InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        if( appPremisesRecommendationDto != null) {
            applicationViewDto.setRecomLiceStartDate(appPremisesRecommendationDto.getRecomInDate());
        }
        if (IaisCommonUtils.isNotEmpty(applicationViewDto.getAppSvcVehicleDtos())) {
            AppPremisesRecommendationDto vehRecom = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorId,InspectionConstants.RECOM_TYPE_INSP_CHECKLIST_VEHICLE).getEntity();
            if (vehRecom != null) {
                applicationViewDto.setInspVehicleNames(JsonUtil.parseToList(vehRecom.getRemarks(), String.class));
            }
        }

        // get Aduit dto
        if(applicationViewDto.getApplicationDto() != null && ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equalsIgnoreCase( applicationViewDto.getApplicationDto().getApplicationType())){
            AppGrpPremisesDto appGrpPremisesDto =   inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorId).getEntity();
            if( appGrpPremisesDto != null){
                LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremisesAuditDtoByLicIdAndHCICode(applicationViewDto.getApplicationDto().getOriginLicenceId(),appGrpPremisesDto.getHciCode()).getEntity();
                if(licPremisesAuditDto != null){
                    licPremisesAuditDto.setIncludeRiskTypeOld(licPremisesAuditDto.getIncludeRiskType());
                }
                applicationViewDto.setLicPremisesAuditDto(licPremisesAuditDto);
            }
        }
        //setMaxFileSize
        applicationViewDto.setSystemMaxFileSize(systemParamConfig.getUploadFileLimit());
        applicationViewDto.setSystemFileType( FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType()));
        try{
            setAppealTypeValues(applicationViewDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        //RollBackHistory remove currentRoleId
        log.info(StringUtil.changeForLog("The currentRoleId is -->:"+currentRoleId));
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = applicationViewDto.getRollBackHistroyList();
        List<AppPremisesRoutingHistoryDto> newAppPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(currentRoleId)&&!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos){
                if(!currentRoleId.equals(appPremisesRoutingHistoryDto.getRoleId())){
                    newAppPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("The newAppPremisesRoutingHistoryDtos.size() is -->:"+newAppPremisesRoutingHistoryDtos.size()));
        applicationViewDto.setRollBackHistroyList(newAppPremisesRoutingHistoryDtos);

        setTcuDate(appCorId,applicationViewDto);

        setAppEditSelectDto(applicationViewDto);

        return applicationViewDto;
    }

    private void initDocuments(ApplicationViewDto applicationViewDto) {
        List<HcsaSvcDocConfigDto> docTitleList = applicationViewService.getTitleById(applicationViewDto.getTitleIdList());
        Map<String, HcsaSvcDocConfigDto> docMap = docTitleList.stream()
                .collect(Collectors.toMap(HcsaSvcDocConfigDto::getId, Function.identity()));
        List<OrgUserDto> userNameList = applicationViewService.getUserNameById(applicationViewDto.getUserIdList());
        Map<String, OrgUserDto> userDtoMap = userNameList.stream()
                .collect(Collectors.toMap(OrgUserDto::getId, Function.identity()));
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        if (applicationDto != null) {
            AppSubmissionDto appSubmissionByAppId = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionByAppId.getAppSvcRelatedInfoDtoList().get(0);
            HcsaServiceDto hcsaServiceDto = DealSessionUtil.initServiceInfo(appSvcRelatedInfoDto, null, false);
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList = DealSessionUtil.initAppPremSpecialisedList(appSubmissionByAppId,
                    IaisCommonUtils.genNewArrayListWithData(hcsaServiceDto), false);
            List<DocumentShowDto> documentShowDtos = DealSessionUtil.initDocumentShowList(appSvcRelatedInfoDto,
                    appPremSpecialisedDtoList, false);
            for (DocumentShowDto documentShowDto : documentShowDtos) {
                List<DocSectionDto> docSectionList = documentShowDto.getDocSectionList();
                if (IaisCommonUtils.isEmpty(docSectionList)) {
                    continue;
                }
                docSectionList.stream()
                        .filter(docSection -> IaisCommonUtils.isNotEmpty(docSection.getDocSecDetailList()))
                        .forEach(docSection -> docSection.getDocSecDetailList().stream()
                                .filter(docSecDetail -> IaisCommonUtils.isNotEmpty(docSecDetail.getAppSvcDocDtoList()))
                                .forEach(docSecDetail -> {
                                    for (AppSvcDocDto dto : docSecDetail.getAppSvcDocDtoList()) {
                                        map.put(docSecDetail.getDocTitle() + dto.getPersonType() + dto.getSeqNum(),
                                                docSecDetail.getDisplayTitle());
                                    }
                                }));
            }
        }

        List<AppSupDocDto> appSupDocDtos = applicationViewDto.getAppSupDocDtoList();
        for (AppSupDocDto appSupDocDto : appSupDocDtos) {
            HcsaSvcDocConfigDto hcsaSvcDocConfigDto = docMap.get(appSupDocDto.getFile());
            if (hcsaSvcDocConfigDto != null) {
                appSupDocDto.setFile(map.get(hcsaSvcDocConfigDto.getDocTitle() + appSupDocDto.getPersonType()
                        + appSupDocDto.getSeqNum()));
            }
            OrgUserDto orgUserDto = userDtoMap.get(appSupDocDto.getSubmittedBy());
            if (orgUserDto != null) {
                appSupDocDto.setSubmittedBy(orgUserDto.getDisplayName());
            }
        }
    }

    private void setAppEditSelectDto(ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if (applicationDto != null) {
            AppEditSelectDto appEditSelectDto = null;
            if (StringUtil.isIn(applicationDto.getApplicationType(),
                    new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, ApplicationConsts.APPLICATION_TYPE_RENEWAL})) {
                //RFC
                String applicationNo = applicationDto.getApplicationNo();
                List<AppEditSelectDto> appEditSelectDtosByAppIds = rfiCanCheck.getAppEditSelectDtoSForRfi(applicationNo);
                if (!appEditSelectDtosByAppIds.isEmpty()) {
                    appEditSelectDto = appEditSelectDtosByAppIds.get(0);
                } else {
                    appEditSelectDto = ApplicationHelper.createAppEditSelectDto(false);
                }
                Optional<String> licenseeType = Optional.ofNullable(applicationViewDto.getSubLicenseeDto())
                        .map(SubLicenseeDto::getLicenseeType)
                        .filter(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL::equals);
                if (!licenseeType.isPresent()) {
                    appEditSelectDto.setLicenseeEdit(false);
                }
            } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())) {
                if (!StringUtil.isEmpty(applicationDto.getId())) {
                    List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_NEW);
                    if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                        appEditSelectDto = appEditSelectDtos.get(0);
                    } else {
                        appEditSelectDto = ApplicationHelper.createAppEditSelectDto(true);
                    }
                }
                //appEditSelectDto = createAppEditSelectDto(true);
                Optional<String> licenseeType = Optional.ofNullable(applicationViewDto.getSubLicenseeDto())
                        .map(SubLicenseeDto::getLicenseeType)
                        .filter(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO::equals);
                if (licenseeType.isPresent() && appEditSelectDto != null) {
                    appEditSelectDto.setLicenseeEdit(false);
                }
            } else {
                appEditSelectDto = ApplicationHelper.createAppEditSelectDto(false);
            }
            applicationViewDto.setAppEditSelectDto(appEditSelectDto);
        }
    }

    private void setTcuDate(String appCorId,ApplicationViewDto applicationViewDto){
        AppPremisesRecommendationDto dto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorId,InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(dto != null && dto.getRecomInDate() != null){
            try {
                applicationViewDto.setTcuFlag(true);
                applicationViewDto.setTuc(Formatter.formatDate(dto.getRecomInDate()));
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
    }

    private void setAppealTypeValues(ApplicationViewDto applicationViewDto){
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            //get appeal type
            String appId = applicationDto.getId();
            List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(appId).getEntity();
            if (premiseMiscDtoList != null) {
                AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
                String appealNo = "";
                String reason = premiseMiscDto.getReason();
                if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)) {
                    String serviceId = applicationViewDto.getApplicationDto().getServiceId();
                    HcsaServiceDto serviceByServiceName=HcsaServiceCacheHelper.getServiceById(serviceId);
                    AppSvcPrincipalOfficersDto appSvcCgoDto = applicationClient.getApplicationCgoByAppId(appId,ApplicationConsts.PERSONNEL_PSN_TYPE_CGO).getEntity();
                    appSvcCgoDto.setAssignSelect("newOfficer");
                    appSvcCgoDto.setSpecialtyGetDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getSpecialtyGetDate(), null));
                    appSvcCgoDto.setCurrRegiDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getCurrRegiDate(), null));
                    appSvcCgoDto.setPraCerEndDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getPraCerEndDate(), null));
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
                    appSvcCgoDtoList.add(appSvcCgoDto);
                    SelectOption sp0 = new SelectOption("-1", "Please Select");
                    List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
                    cgoSelectList.add(sp0);
                    SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
                    cgoSelectList.add(sp1);
                    List<SelectOption> idTypeSelOp = getIdTypeSelOp();
                    if (serviceByServiceName != null) {
                        List<SelectOption> list = genSpecialtySelectList(serviceByServiceName.getSvcCode());
                        applicationViewDto.setSpecialtySelectList(list);
                    }
                    applicationViewDto.setCgoMandatoryCount(1);
                    applicationViewDto.setGovernanceOfficersList(appSvcCgoDtoList);
                    applicationViewDto.setCgoSelectList(cgoSelectList);
                    applicationViewDto.setIdTypeSelect(idTypeSelOp);
                }
                //file
                List<AppPremisesSpecialDocDto> appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                if(appealSpecialDocDto != null){
                    appealSpecialDocDto.sort(Comparator.comparing(AppPremisesSpecialDocDto::getIndex));
                    applicationViewDto.setFeAppealSpecialDocDto(appealSpecialDocDto);
                }
                String oldAppId = premiseMiscDto.getRelateRecId();
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(oldApplication.getId()).getEntity();
                    if(appPremisesCorrelationDtos != null && appPremisesCorrelationDtos.size()>0){
                        AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
                        AppInsRepDto appInsRepDto = appPremisesCorrClient.appGrpPremises(appPremisesCorrelationDto.getId()).getEntity();
                        if(appInsRepDto != null){
                            String hciName = appInsRepDto.getHciName();
                            List<String> hciNames = IaisCommonUtils.genNewArrayList();
                            if(!StringUtil.isEmpty(hciName)){
                                hciNames.add(hciName);
                                applicationViewDto.setHciNames(hciNames);
                            }
                        }
                    }
                    appealNo = oldApplication.getApplicationNo();
                }
                String appealType = premiseMiscDto.getAppealType();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)){
                    LicenceDto licenceDto = licenceService.getLicenceDto(premiseMiscDto.getRelateRecId());
                    appealNo = licenceDto.getLicenceNo();
                }
                applicationViewDto.setAppealNo(appealNo);
                applicationViewDto.setPremiseMiscDto(premiseMiscDto);
            }
        }
    }

    private List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", "Please Select");
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    private List<SelectOption> genSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }
        }
        return specialtySelectList;
    }


    @Override
    public void clearApprovedHclCodeByExistRejectApp(List<ApplicationDto> saveApplicationDtoList, String appGroupType,ApplicationDto applicationDtoMain) {
        log.info("-----------clearApprovedHclCodeByExistRejectApp start------");
        if(saveApplicationDtoList.size() > 1 && ( ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(appGroupType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appGroupType))){
            int size = saveApplicationDtoList.size();
            List<ApplicationDto> appovedNum = new ArrayList<>(size);
            List<ApplicationDto> rejectNum = new ArrayList<>(size);
            for(ApplicationDto applicationDto : saveApplicationDtoList){
                if(ApplicationConsts.APPLICATION_STATUS_APPROVED .equalsIgnoreCase(applicationDto.getStatus())){
                    appovedNum.add(applicationDto);
                }else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(applicationDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equalsIgnoreCase(applicationDto.getStatus())){
                    rejectNum.add(applicationDto);
                    if(applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                        List<AppEditSelectDto> appEditSelectDtos = applicationClient.getAppEditSelectDto(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC).getEntity();
                        boolean changePrem=false;
                        for (AppEditSelectDto edit : appEditSelectDtos) {
                            if (edit.isPremisesEdit() || edit.isPremisesListEdit()) {
                                changePrem = true;
                            }
                        }
                        if(changePrem){
                            List<ApplicationDto> apps=IaisCommonUtils.genNewArrayList();
                            apps.add(applicationDto);
                            applicationClient.clearHclcodeByAppIds(apps);
                        }
                    }
                }
            }
            if(appovedNum.size() > 0 && rejectNum.size() >0){
                //clear approve hclcode
                appovedNum.get(0).setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                // set main appoved true
                for(ApplicationDto applicationDto : appovedNum){
                    if("0".equalsIgnoreCase(String.valueOf(applicationDto.getSecondaryFloorNoChange()))){
                        applicationDto.setNeedNewLicNo(true);
                        if(applicationDtoMain.getId().equalsIgnoreCase(applicationDto.getId())){
                            applicationDtoMain.setNeedNewLicNo(true);
                        }
                    }
                }
            }
        }
        log.info("-----------clearApprovedHclCodeByExistRejectApp end------");
    }

    @Override
    public boolean noContainsFlagByStageList(List<SelectOption> nextStageList) {
        //contains ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL, No -> true
        return nextStageList.stream().noneMatch(stage -> ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(stage.getValue()));
    }
}
