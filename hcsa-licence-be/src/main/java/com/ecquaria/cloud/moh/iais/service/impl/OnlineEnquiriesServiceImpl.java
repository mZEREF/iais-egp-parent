package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRectifiedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportNcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.EnquiryInspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.DisciplinaryRecordResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.RegistrationDetailDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OnlineEnquiriesServiceImpl
 *
 * @author junyu
 * @date 2020/2/11
 */
@Service
@Slf4j
public class OnlineEnquiriesServiceImpl implements OnlineEnquiriesService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    InspectionRectificationProService inspectionRectificationProService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private InsRepClient insRepClient;
    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    private InsRepService insRepService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    TaskService taskService;
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    AcraUenBeClient acraUenBeClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Override
    @SearchTrack(catalog = "ReqForInfoQuery", key = "licenseeQuery")
    public SearchResult<LicenseeQueryDto> searchLicenseeIdsParam(SearchParam searchParam) {
        return organizationClient.searchLicenseeIdsParam(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "ReqForInfoQuery", key = "serviceQuery")
    public SearchResult<HcsaSvcQueryDto> searchSvcNamesParam(SearchParam searchParam) {
        return hcsaConfigClient.searchSvcNamesParam(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "onlineEnquiry", key = "searchByProfessionalInfo")
    public SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam) {
        return hcsaLicenceClient.searchProfessionalInformation(searchParam).getEntity();
    }

    @Override
    public void setLicInfo(HttpServletRequest request) {
        String licenceId = (String) ParamUtil.getSessionAttr(request, "id");

        LicenceDto licenceDto=hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
        OrganizationLicDto organizationLicDto= organizationClient.getOrganizationLicDtoByLicenseeId(licenceDto.getLicenseeId()).getEntity();
        try{
            organizationLicDto.getLicenseeDto().setLicenseeType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{organizationLicDto.getLicenseeDto().getLicenseeType()}).get(0).getText());
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
        }
        try{
            organizationLicDto.getLicenseeIndividualDto().setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{organizationLicDto.getLicenseeIndividualDto().getSalutation()}).get(0).getText());
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
        }

        List<PersonnelsDto> personnelsDto= hcsaLicenceClient.getPersonnelDtoByLicId(licenceId).getEntity();

        for (LicenseeKeyApptPersonDto org:organizationLicDto.getLicenseeKeyApptPersonDtos()
        ) {
            try {
                org.setDesignation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getDesignation()}).get(0).getText());

            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
            try {
                org.setIdType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getIdType()}).get(0).getText());

            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
            try {
                org.setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{org.getSalutation()}).get(0).getText());
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
        }
        for (PersonnelsDto per:personnelsDto
        ) {
            try{
                per.getLicKeyPersonnelDto().setPsnType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getLicKeyPersonnelDto().getPsnType()}).get(0).getText());
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
            try {
                per.getKeyPersonnelDto().setSalutation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getSalutation()}).get(0).getText());
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
            try {
                per.getKeyPersonnelDto().setDesignation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelDto().getDesignation()}).get(0).getText());
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
            try {
                per.getKeyPersonnelExtDto().setProfessionType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{per.getKeyPersonnelExtDto().getProfessionType()}).get(0).getText());
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
                per.setKeyPersonnelExtDto(new KeyPersonnelExtDto());
            }
            try {
                switch (per.getKeyPersonnelExtDto().getPreferredMode()){
                    case "1":per.getKeyPersonnelExtDto().setPreferredMode("Email");break;
                    case "2":per.getKeyPersonnelExtDto().setPreferredMode("SMS");break;
                    case "3":per.getKeyPersonnelExtDto().setPreferredMode("Email  SMS");break;
                    default:per.getKeyPersonnelExtDto().setPreferredMode("-");break;
                }
            }catch (NullPointerException e){
                log.error(e.getMessage(), e);
            }
        }
        List<ComplianceHistoryDto> complianceHistoryDtos= IaisCommonUtils.genNewArrayList();
        Set<String> appIds=IaisCommonUtils.genNewHashSet();
        complianceHistoryDtos= complianceHistoryDtosByLicId(complianceHistoryDtos,licenceId,appIds);
        ParamUtil.setSessionAttr(request,"registeredWithACRA","Not Registered");
        try {
            GenerateUENDto generateUENDto = acraUenBeClient.getUen(organizationLicDto.getUenNo()).getEntity();
            if(generateUENDto.getBasic().getIsRegisteredWithACRA()){
                ParamUtil.setSessionAttr(request,"registeredWithACRA","Registered");
            }else {
                ParamUtil.setSessionAttr(request,"registeredWithACRA","Not Registered");
            }
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        complianceHistoryDtos.sort(Comparator.comparing(ComplianceHistoryDto::getSortDate));
        ParamUtil.setSessionAttr(request,"complianceHistoryDtos", (Serializable) complianceHistoryDtos);
        ParamUtil.setSessionAttr(request,"organizationLicDto",organizationLicDto);
        ParamUtil.setSessionAttr(request,"personnelsDto", (Serializable) personnelsDto);
    }

    @Override
    public List<ComplianceHistoryDto> complianceHistoryDtosByLicId(List<ComplianceHistoryDto> complianceHistoryDtos,String licenceId,Set<String> appIds){
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceId).getEntity();
        for(LicAppCorrelationDto appCorrelationDto:licAppCorrelationDtos){
            if(!appIds.contains(appCorrelationDto.getApplicationId())){
                ComplianceHistoryDto complianceHistoryDto=new ComplianceHistoryDto();
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appCorrelationDto.getApplicationId()).getEntity();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appCorrelationDto.getApplicationId()).getEntity();
                appIds.add(appCorrelationDto.getApplicationId());
                ApplicationGroupDto applicationGroupDto=applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                complianceHistoryDto.setInspectionTypeName(applicationGroupDto.getIsPreInspection() == 0? "Off-Site Inspection":InspectionConstants.INSPECTION_TYPE_ONSITE);
                complianceHistoryDto.setAppPremCorrId(appPremisesCorrelationDto.getId());

                //add listReportNcRectifiedDto and add ncItemId
                AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremisesCorrelationDto.getId()).getEntity();
                if (appPremPreInspectionNcDto != null) {
                    String ncId = appPremPreInspectionNcDto.getId();
                    List<AppPremisesPreInspectionNcItemDto> listAppPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
                    if (listAppPremisesPreInspectionNcItemDtos != null && !listAppPremisesPreInspectionNcItemDtos.isEmpty()) {
                        complianceHistoryDto.setComplianceTag("Partial ");
                    }
                } else {
                    complianceHistoryDto.setComplianceTag("Full ");
                }
                if(applicationDto.getOriginLicenceId()!=null){
                    complianceHistoryDtosByLicId(complianceHistoryDtos,applicationDto.getOriginLicenceId(),appIds);
                }
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                try {
                    complianceHistoryDto.setRemarks(appPremisesRecommendationDto.getRemarks());
                    HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setLicId(licenceId);
                    List<ApplicationDto> applicationDtos = new ArrayList<>(1);
                    applicationDto.setNeedInsp(true);
                    applicationDtos.add(applicationDto);
                    hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                    String riskLevel = entity.getRiskLevel();
                    complianceHistoryDto.setRiskTag(MasterCodeUtil.retrieveOptionsByCodes(new String[]{riskLevel}).get(0).getText());
                }catch (NullPointerException e){
                    log.error(e.getMessage(), e);
                    complianceHistoryDto.setRiskTag("-");
                }
                AppPremisesRecommendationDto appPreRecommentdationDtoDate = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                try {
                    complianceHistoryDto.setInspectionDate(Formatter.formatDateTime(appPreRecommentdationDtoDate.getRecomInDate(), AppConsts.DEFAULT_DATE_FORMAT));
                    complianceHistoryDto.setSortDate(Formatter.formatDateTime(appPreRecommentdationDtoDate.getRecomInDate(), "yyyy-MM-dd"));
                    AppPremisesRecommendationDto appPreRecommentdationDtoRep = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                    if(appPreRecommentdationDtoRep!=null){
                        complianceHistoryDtos.add(complianceHistoryDto);
                    }
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                    complianceHistoryDto.setInspectionDate("-");
                }
            }

        }
        return complianceHistoryDtos;
    }

    @Override
    public PersonnelsDto getProfessionalInformationByKeyPersonnelId(String psnId) {
        return hcsaLicenceClient.getProfessionalInformationByKeyPersonnelId(psnId).getEntity();
    }

    @Override
    public RegistrationDetailDto getRegnDetailListByRegnNo(String regnNo) {
        ProfessionalParameterDto professionalParameterDto =new ProfessionalParameterDto();
        professionalParameterDto.setRegNo(Collections.singletonList(regnNo));
        professionalParameterDto.setClientId("22222");
        professionalParameterDto.setTimestamp(DateUtil.formatDateTime(new Date(), "yyyyMMddHHmmssSSS"));
        professionalParameterDto.setSignature("2222");
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<DisciplinaryRecordResponseDto> list = beEicGatewayClient.getDisciplinaryRecord(professionalParameterDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        RegistrationDetailDto detail = new RegistrationDetailDto();


        return detail;
    }

    @Override
    public EnquiryInspectionReportDto getInsRepDto(ApplicationViewDto applicationViewDto,String licenceId)  {
        EnquiryInspectionReportDto inspectionReportDto = new EnquiryInspectionReportDto();
        List<PremisesDto> licPremisesDto=hcsaLicenceClient.getPremisess(licenceId).getEntity();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto ncRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        //inspection report application dto
        AppInsRepDto appInsRepDto = insRepClient.getAppInsRepDto(appPremisesCorrelationId).getEntity();
        inspectionReportDto.setHciCode(appInsRepDto.getHciCode());
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        for (PremisesDto appGrpPremise:licPremisesDto
        ) {
            String adderss = MiscUtil.getAddress(appGrpPremise.getUnitNo(), appGrpPremise.getStreetName()
                    ,appGrpPremise.getBuildingName(),appGrpPremise.getFloorNo(),appGrpPremise.getUnitNo(),appGrpPremise.getPostalCode());
            if(adderss.equals(appInsRepDto.getHciAddress())){
                inspectionReportDto.setHciCode(appGrpPremise.getHciCode());
            }
        }
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String appGrpId = applicationDto.getAppGrpId();
        String status = applicationDto.getStatus();
        String appTypeCode = applicationDto.getApplicationType();
        ApplicationGroupDto applicationGroupDto = insRepClient.getApplicationGroupDto(appGrpId).getEntity();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(appInsRepDto.getLicenseeId()).getEntity();
        if(StringUtil.isEmpty(licenceId)){
            inspectionReportDto.setLicenceNo("-");
        }else{
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
            if(licenceDto!=null){
                String licenceNo = licenceDto.getLicenceNo();
                inspectionReportDto.setLicenceNo(licenceNo);
            }
        }
        if(licenseeDto!=null){
            String name = licenseeDto.getName();
            inspectionReportDto.setLicenseeName(name);
        }
        List<AppGrpPersonnelDto> principalOfficer = appInsRepDto.getPrincipalOfficer();
        List<String> poNames = IaisCommonUtils.genNewArrayList();
        for(AppGrpPersonnelDto appGrpPersonnelDto : principalOfficer){
            String name = appGrpPersonnelDto.getName();
            poNames.add(name);
        }
        inspectionReportDto.setPrincipalOfficers(poNames);


        List<String> nameList = IaisCommonUtils.genNewArrayList();
        AppPremisesRecommendationDto otherOfficesDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
        if(otherOfficesDto!=null){
            String otherOffices = otherOfficesDto.getRemarks();
            nameList.add(otherOffices);
            inspectionReportDto.setInspectOffices(nameList);
        }else {
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
        try{
            HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
            hcsaRiskScoreDto.setAppType(applicationType);
            hcsaRiskScoreDto.setLicId(licenceId);
            List<ApplicationDto> applicationDtos = new ArrayList<>(1);
            applicationDto.setNeedInsp(true);
            applicationDtos.add(applicationDto);
            hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
            hcsaRiskScoreDto.setServiceId(serviceId);
            HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
            String riskLevel = entity.getRiskLevel();
            inspectionReportDto.setRiskLevel(MasterCodeUtil.retrieveOptionsByCodes(new String[]{riskLevel}).get(0).getText());
        }catch (Exception e){
            inspectionReportDto.setRiskLevel("-");
            log.info(e.getMessage(),e);
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
        List<ReportNcRegulationDto> listReportNcRegulationDto = IaisCommonUtils.genNewArrayList();
        List<ReportNcRectifiedDto> listReportNcRectifiedDto = IaisCommonUtils.genNewArrayList();
        //add ReportNcRegulationDto and add ncItemId
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

        //best practice remarks
        String bestPractice = "-";
        String remarks = "-";
        if(ncRecommendationDto==null){
            inspectionReportDto.setMarkedForAudit("No");
        }else if(ncRecommendationDto!=null) {
            Date recomInDate = ncRecommendationDto.getRecomInDate();
            if(recomInDate==null){
                inspectionReportDto.setMarkedForAudit("No");
            }else {
                inspectionReportDto.setMarkedForAudit("Yes");
                inspectionReportDto.setTcuDate(recomInDate);
            }
            String ncBestPractice = ncRecommendationDto.getBestPractice();
            String ncRemarks = ncRecommendationDto.getRemarks();
            if(!StringUtil.isEmpty(ncBestPractice)){
                bestPractice = ncBestPractice ;
            }
            if(!StringUtil.isEmpty(ncRemarks)){
                remarks = ncRemarks ;
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

        //get reported By

        Set<String> inspectior = taskService.getInspectiors(applicationDto.getApplicationNo(), TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT, RoleConsts.USER_ROLE_INSPECTIOR);
        Set<String> ao1s=taskService.getInspectiors(applicationDto.getApplicationNo(),TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1, RoleConsts.USER_ROLE_AO1);
        List<String> iteraterInspectior= IaisCommonUtils.genNewArrayList();
        iteraterInspectior.addAll(inspectior);
        List<String> iteraterAo1s= IaisCommonUtils.genNewArrayList();
        iteraterAo1s.addAll(ao1s);
        //get inspection lead
        List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(iteraterInspectior).getEntity();
        try{
            String reportBy = userList.get(0).getDisplayName();
            inspectionReportDto.setReportedBy(reportBy);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        List<OrgUserDto> leadList = organizationClient.retrieveOrgUserAccount(iteraterAo1s).getEntity();
        try{
            String leadName = leadList.get(0).getDisplayName();
            inspectionReportDto.setReportNoteBy(leadName);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

        Set<String> inspectiors = taskService.getInspectiors(applicationDto.getApplicationNo(), TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR);
        List<String> inspectors = IaisCommonUtils.genNewArrayList();
        inspectors.addAll(inspectiors);
        List<OrgUserDto> inspectorList = organizationClient.retrieveOrgUserAccount(inspectors).getEntity();
        List<String> inspectorsName = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto orgUserDto : inspectorList) {
            String displayName = orgUserDto.getDisplayName();
            inspectorsName.add(displayName);
        }
        inspectionReportDto.setInspectors(inspectorsName);
//        inspectionReportDto.setReportedBy(reportBy);
//        inspectionReportDto.setReportNoteBy(leadName);
        inspectionReportDto.setServiceName(svcName);
        inspectionReportDto.setHciName(appInsRepDto.getHciName());
        inspectionReportDto.setHciAddress(appInsRepDto.getHciAddress());
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
    public void preInspReport(HttpServletRequest request) {
        log.info("=======>>>>>preInspReport>>>>>>>>>>>>>>>>requestForInformation");
        String appPremisesCorrelationId=ParamUtil.getMaskedString(request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        String licenceId = (String) ParamUtil.getSessionAttr(request, "id");


        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appPremisesCorrelationId);
        EnquiryInspectionReportDto insRepDto = getInsRepDto(applicationViewDto,licenceId);
        try{
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
            if(appPremisesRecommendationDto.getRecomDecision().equals(InspectionReportConstants.APPROVED)||appPremisesRecommendationDto.getRecomDecision().equals(InspectionReportConstants.APPROVEDLTC)||appPremisesRecommendationDto.getRecomDecision().equals(InspectionReportConstants.RFC_APPROVED)){
                int recomInNumber=appPremisesRecommendationDto.getRecomInNumber();
                String chronoUnit=MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRecommendationDto.getChronoUnit()}).get(0).getText();
                if(appPremisesRecommendationDto.getChronoUnit().equals(AppointmentConstants.RECURRENCE_MONTH)){
                    if(recomInNumber/12 ==1 || recomInNumber/12 >1){
                        recomInNumber=recomInNumber/12;
                        chronoUnit=MasterCodeUtil.retrieveOptionsByCodes(new String[]{AppointmentConstants.RECURRENCE_YEAR}).get(0).getText();
                    }
                }
                insRepDto.setRecommendation("Approve with "+recomInNumber+" "+chronoUnit+" Licence");
            }else {
                insRepDto.setRecommendation(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRecommendationDto.getRecomDecision()}).get(0).getText());
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            insRepDto.setRecommendation("-");
        }
        AppPremisesRecommendationDto appPremisesRecommendationDto =initRecommendation(appPremisesCorrelationId, applicationViewDto);
        ParamUtil.setRequestAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setRequestAttr(request, "insRepDto", insRepDto);
        // 		preInspReport->OnStepProcess
    }

    private AppPremisesRecommendationDto initRecommendation(String correlationId, ApplicationViewDto applicationViewDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            String period = recomInNumber + " " + chronoUnit;
            List<String> periods = insRepService.getPeriods(applicationViewDto);
            if (periods != null && !periods.isEmpty() && !InspectionReportConstants.REJECTED.equals(recomDecision)) {
                if (periods.contains(period)) {
                    initRecommendationDto.setPeriod(period);
                } else {
                    initRecommendationDto.setPeriod("Others");
                    initRecommendationDto.setRecomInNumber(recomInNumber);
                    initRecommendationDto.setChronoUnit(chronoUnit);
                }
            } else if (InspectionReportConstants.REJECTED.equals(recomDecision)) {
                initRecommendationDto.setPeriod(null);
            }
            initRecommendationDto.setRecommendation(recomDecision);
        }

        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            initRecommendationDto.setRemarks(reportRemarks);
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        return initRecommendationDto;
    }
    @Override
    public void setAppInfo(HttpServletRequest request) {
        String appCorrId = (String) ParamUtil.getSessionAttr(request, "id");
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(appCorrId);
        if(applicationViewDto.getHciCode()==null){
            List<String> appIds= new ArrayList<>(Collections.singleton(applicationViewDto.getApplicationDto().getId()));
            List<String> licIds=getLicIdsByappIds(appIds);
            if(licIds.size()!=0){
                List<PremisesDto> premisesDtoList = hcsaLicenceClient.getPremisess(licIds.get(0)).getEntity();
                applicationViewDto.setHciCode(premisesDtoList.get(0).getHciCode());
            }
        }
        AppInsRepDto appInsRepDto=insRepClient.getAppInsRepDto(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationViewDto.getApplicationDto().getId());
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(appInsRepDto.getLicenseeId());
        try{
            licenseeDto.setEmilAddr(IaisEGPHelper.getLicenseeEmailAddrs(appInsRepDto.getLicenseeId()).get(0));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        if(appSvcRelatedInfoDto != null){
            String serviceId = appSvcRelatedInfoDto.getServiceId();
            hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        if( allocationDto !=null && allocationDto.size()>0 ){
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){

                appSvcDisciplineAllocationDto.setPremiseVal(applicationViewDto.getHciAddress());
                List<AppSvcChckListDto> appSvcChckListDtoList = null;
                String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                //set chkLstName
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                    for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                    }
                }
                if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                    for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                        if(hcsaSvcSubtypeOrSubsumedDto!=null){
                            appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                        }
                        if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                        }
                    }
                }
                //set selCgoName
                List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                    for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                        if(idNo.equals(appSvcCgoDto.getIdNo())){
                            appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                        }
                    }
                }
            }
        }

        String appType= MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationType()}).get(0).getText();
        applicationViewDto.setApplicationType(appType);
        applicationViewDto.getApplicationDto().setApplicationType(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
        List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos=applicationClient.getAppSvcPremisesScopeListByCorreId(appCorrId).getEntity();
        for (AppSvcPremisesScopeDto a:appSvcPremisesScopeDtos
             ) {
            HcsaServiceSubTypeDto hcsaServiceSubTypeDto=hcsaConfigClient.getHcsaServiceSubTypeById(a.getScopeName()).getEntity();
            if(hcsaServiceSubTypeDto!=null&&hcsaServiceSubTypeDto.getSubtypeName()!=null){
                a.setScopeName(hcsaServiceSubTypeDto.getSubtypeName());
            }
        }
        try {
            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtos=organizationClient.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeDto.getId()).getEntity();
            ParamUtil.setRequestAttr(request,"licenseeKeyApptPersonDtos",licenseeKeyApptPersonDtos);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        ParamUtil.setRequestAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setRequestAttr(request,"appSvcRelatedInfoDto",appSvcRelatedInfoDto);
        ParamUtil.setRequestAttr(request,"licenseeDto",licenseeDto);
        ParamUtil.setRequestAttr(request,"serviceStep",appSvcPremisesScopeDtos);
        // 		preAppInfo->OnStepProcess
    }

    private HcsaSvcSubtypeOrSubsumedDto getHcsaSvcSubtypeOrSubsumedDtoById(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,String id){
        HcsaSvcSubtypeOrSubsumedDto result = null;
        if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)&&!StringUtil.isEmpty(id)){
            for (HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto : hcsaSvcSubtypeOrSubsumedDtos){
                if(id.equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                    result = hcsaSvcSubtypeOrSubsumedDto;
                    break;
                }else{
                    result = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDto.getList(),id);
                    if(result!=null){
                        break;
                    }
                }
            }
        }
        return result;
    }
    @Override
    public List<SelectOption> getServicePersonnelRoleOption() {
        List<SelectOption> svcPerRoleOption=IaisCommonUtils.genNewArrayList(4);
        SelectOption selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        svcPerRoleOption.add(selectOption);
        selectOption=new SelectOption();
        selectOption.setText(ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT);
        selectOption.setValue(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        svcPerRoleOption.add(selectOption);
        svcPerRoleOption.sort(Comparator.comparing(SelectOption::getText));
        return svcPerRoleOption;
    }

    @Override
    public List<String> getLicIdsByappIds(List<String> appIds) {
        List<String> licIds =IaisCommonUtils.genNewArrayList();
        List<LicAppCorrelationDto> appCorrelationDtoList=IaisCommonUtils.genNewArrayList();
        for (String appId:appIds
        ) {
            List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrByappId(appId).getEntity();
            if(licAppCorrelationDtos.size()>0){
                appCorrelationDtoList.addAll(licAppCorrelationDtos);
            }
        }
        for (LicAppCorrelationDto licAppCorrelationDto:appCorrelationDtoList
        ) {
            licIds.add(licAppCorrelationDto.getLicenceId());
        }
        HashSet<String> set = new HashSet<>(licIds);
        licIds.clear();
        licIds.addAll(set);
        return licIds;
    }
}
